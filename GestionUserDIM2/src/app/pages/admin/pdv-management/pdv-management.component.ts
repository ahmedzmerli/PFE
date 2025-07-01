import { Component, OnInit } from '@angular/core';
import { PdvService } from "../../../services/pdv.service";
import { PdvMaster } from "../../../models/pdvmaster.model";
import Swal from 'sweetalert2';

@Component({
  selector: 'app-pdv-management',
  templateUrl: './pdv-management.component.html',
  styleUrls: ['./pdv-management.component.scss']
})
export class PdvManagementComponent implements OnInit {
  cols = [
    { field: 'msisdn', header: 'MSISDN' },
    { field: 'nomPdv', header: 'Nom PDV' },
    { field: 'adresse', header: 'Adresse' },
    { field: 'codePdv', header: 'Code PDV' },
    { field: 'Action', header: 'Action' }
  ];
  selectedColumns = [...this.cols];
  pdvs: PdvMaster[] = [];
  filteredResults: PdvMaster[] = [];
  msisdn: string = '';

  // Modal ajout
  displayAddModal = false;
  addPdvForm: Partial<PdvMaster> = {
    msisdn: '',
    nomPdv: '',
    adresse: '',
    codePdv: ''
  };

  constructor(private pdvService: PdvService) {}

  ngOnInit() {
    this.fetchPdvs();
  }

  fetchPdvs() {
    this.pdvService.getAll().subscribe(data => {
      this.pdvs = data;
      this.filteredResults = data;
    });
  }

  applyFilter() {
    this.filteredResults = this.pdvs.filter(pdv => {
      let valid = true;
      if (this.msisdn && !pdv.msisdn.includes(this.msisdn)) valid = false;
      return valid;
    });
  }

  resetFilters() {
    this.msisdn = '';
    this.filteredResults = [...this.pdvs];
  }

  resetColumns() {
    this.selectedColumns = [...this.cols];
  }

  openAddModal() {
    this.addPdvForm = { msisdn: '', nomPdv: '', adresse: '', codePdv: '' };
    this.displayAddModal = true;
  }

  closeAddModal() {
    this.displayAddModal = false;
  }

  addPdv() {
    if (!this.addPdvForm.msisdn || !this.addPdvForm.nomPdv) {
      Swal.fire('Champs obligatoires', 'Veuillez remplir MSISDN et Nom PDV', 'warning');
      return;
    }
    this.pdvService.create(this.addPdvForm as PdvMaster).subscribe({
      next: () => {
        this.fetchPdvs();
        Swal.fire('Succès', 'PDV ajouté', 'success');
        this.closeAddModal();
      },
      error: err => {
        Swal.fire('Erreur', 'Échec de l\'ajout', 'error');
      }
    });
  }

  deletePdv(msisdn: string) {
    Swal.fire({
      title: 'Êtes-vous sûr ?',
      text: 'Ce PDV sera supprimé définitivement.',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#dc3545',
      cancelButtonColor: '#6c757d',
      confirmButtonText: 'Oui, supprimer',
      cancelButtonText: 'Annuler'
    }).then((result) => {
      if (result.isConfirmed) {
        this.pdvService.delete(msisdn).subscribe({
          next: () => {
            this.fetchPdvs();
            Swal.fire('Supprimé', 'Le PDV a été supprimé.', 'success');
          },
          error: err => {
            Swal.fire('Erreur', 'Suppression impossible', 'error');
          }
        });
      }
    });
  }
}
