import { Component, OnInit } from '@angular/core';
import { Pdv } from 'src/app/models/pdv.model';
import { PdvService } from 'src/app/services/pdv.service';
import { MessageService } from 'primeng/api';
import {LinkMsisdnsRequest, PdvLite} from 'src/app/models/LinkMsisdnsRequest.model';
import Swal from "sweetalert2";
import {PdvGrouped} from "../../../models/pdvgrouped.model";

@Component({
  selector: 'app-pdv-management',
  templateUrl: './pdv-management.component.html',
  styleUrls: ['./pdv-management.component.scss'],
  providers: [MessageService]
})
export class PdvManagementComponent implements OnInit {
  // Liste complète des PDVs récupérés du backend
  pdvs: PdvGrouped[] = [];

  // Résultat filtré en fonction de la recherche
  filteredResults: PdvGrouped[] = [];

  // Colonnes sélectionnées
  cols = [
    { field: 'msisdns', header: 'MSISDN' },
    { field: 'nomPdv', header: 'Nom PDV' },
    { field: 'adresse', header: 'Adresse' },
    { field: 'codePdv', header: 'Code PDV' },
    { field: 'Action', header: 'Actions' }
  ];

  selectedColumns = [...this.cols];

  // Recherche
  msisdn = '';

  // Modals
  displayAddModal = false;
  displayAddMsisdnModal = false;
  displayEditModal = false;
  displayLinkModal = false;

  // Formulaires
  addPdvForm: Partial<Pdv> = {};
  editPdvForm: Partial<Pdv> = {};
  linkPdvForm: PdvLite = { nomPdv: '' };
  linkMsisdns = '';

  // Pour savoir sur quel PDV on ajoute un msisdn
  currentEditPdvId: number | undefined;
  currentAddMsisdnPdvId: number | undefined;

  // Nouveau msisdn à ajouter
  newMsisdn = '';




  displayRemoveMsisdnModal = false;
  selectedMsisdnToRemove = '';
  currentRemoveMsisdnPdvId?: number;
  msisdnsToRemove: string[] = [];


  constructor(
    private pdvService: PdvService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.loadPdvs();
  }

  loadPdvs() {
    this.pdvService.listAll().subscribe({
      next: (data) => {
        const map = new Map<number, PdvGrouped>();

        data.forEach(p => {
          if (!map.has(p.idPdvMaster!)) {
            map.set(p.idPdvMaster!, {
              idPdvMaster: p.idPdvMaster!,
              nomPdv: p.nomPdv,
              adresse: p.adresse,
              codePdv: p.codePdv,
              msisdns: [p.msisdn]
            });
          } else {
            map.get(p.idPdvMaster!)!.msisdns.push(p.msisdn);
          }
        });

        this.pdvs = Array.from(map.values());
        this.filteredResults = [...this.pdvs];
      },
      error: () => {
        Swal.fire('Erreur', 'Impossible de charger les PDVs.', 'error');
      }
    });
  }

  applyFilter() {
    const term = this.msisdn.trim().toLowerCase();
    if (term) {
      this.filteredResults = this.pdvs.filter(p => p.msisdns.some(m => m.includes(term)));
    } else {
      this.filteredResults = [...this.pdvs];
    }
  }



  resetFilters() {
    this.msisdn = '';
    this.filteredResults = [...this.pdvs];
  }

  resetColumns() {
    this.selectedColumns = [...this.cols];
  }

  // ---------------- Ajout PDV ----------------
  openAddModal() {
    this.addPdvForm = {};
    this.displayAddModal = true;
  }

  closeAddModal() {
    this.displayAddModal = false;
  }

  addPdv() {
    if (!this.addPdvForm.msisdn || !this.addPdvForm.nomPdv) {
      return;
    }
    this.pdvService.create(this.addPdvForm as Pdv).subscribe({
      next: () => {
        Swal.fire('Succès', 'Le PDV a été créé avec succès.', 'success');
        this.loadPdvs();
        this.closeAddModal();
      },

      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Erreur', detail: 'Échec de la création' });
      }
    });
  }

  // ---------------- Ajout MSISDN ----------------
  openAddMsisdnModal(id: number) {
    this.currentAddMsisdnPdvId = id;
    this.newMsisdn = '';
    this.displayAddMsisdnModal = true;
  }

  closeAddMsisdnModal() {
    this.displayAddMsisdnModal = false;
  }

  addMsisdn() {
    if (!this.currentAddMsisdnPdvId || !this.newMsisdn) {
      return;
    }
    this.pdvService.addMsisdn(this.currentAddMsisdnPdvId, this.newMsisdn).subscribe({
      next: () => {
        Swal.fire('Succès', 'Le MSISDN a été ajouté.', 'success');
        this.loadPdvs();
        this.closeAddMsisdnModal();
      },

      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Erreur', detail: 'Échec de l\'ajout' });
      }
    });
  }

  // ---------------- Edition PDV ----------------
  openEditModal(row: Pdv) {
    this.currentEditPdvId = row.idPdvMaster;
    this.editPdvForm = {
      nomPdv: row.nomPdv,
      adresse: row.adresse,
      codePdv: row.codePdv
    };
    this.displayEditModal = true;
  }

  closeEditModal() {
    this.displayEditModal = false;
  }

  updatePdv() {
    if (!this.currentEditPdvId) {
      return;
    }
    this.pdvService.update(this.currentEditPdvId, this.editPdvForm).subscribe({
      next: () => {
        Swal.fire('Succès', 'Le PDV a été mis à jour.', 'success');
        this.loadPdvs();
        this.closeEditModal();
      },
      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Erreur', detail: 'Échec de la mise à jour' });
      }
    });
  }

  // ---------------- Suppression MSISDN ----------------
  deletePdv(row: PdvGrouped) {
    Swal.fire({
      title: 'Confirmer la suppression',
      text: `Voulez-vous vraiment supprimer ce PDV ?`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#d33',
      cancelButtonColor: '#6c757d',
      confirmButtonText: 'Oui, supprimer',
      cancelButtonText: 'Annuler'
    }).then((result) => {
      if (result.isConfirmed) {
        // On utilise le premier MSISDN pour identifier le PDV
        this.pdvService.deletePdvMaster(row.idPdvMaster).subscribe({
          next: () => {
            Swal.fire('Supprimé !', 'Le PDV a été supprimé.', 'success');
            this.loadPdvs();
          },
          error: () => {
            Swal.fire('Erreur', 'La suppression a échoué.', 'error');
          }
        });

      }
    });
  }



  // ---------------- Liaison MSISDN existants ----------------
  openLinkModal() {
    this.linkPdvForm = { nomPdv: '' };
    this.linkMsisdns = '';
    this.displayLinkModal = true;
  }

  closeLinkModal() {
    this.displayLinkModal = false;
  }

  linkMsisdnsToPdv() {
    const msisdnArray = this.linkMsisdns.split(',').map(s => s.trim()).filter(s => s);
    if (!this.linkPdvForm.nomPdv || msisdnArray.length === 0) {
      return;
    }
    const payload: LinkMsisdnsRequest = {
      msisdns: msisdnArray,
      pdv: {
        nomPdv: this.linkPdvForm.nomPdv!,
        adresse: this.linkPdvForm.adresse,
        codePdv: this.linkPdvForm.codePdv
      }
    };

    this.pdvService.linkExistingMsisdns(payload).subscribe({
      next: () => {
        Swal.fire('Succès', 'Les MSISDN ont été liés au nouveau PDV.', 'success');
        this.loadPdvs();
        this.closeLinkModal();
      },

      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Erreur', detail: 'Échec de la liaison' });
      }
    });
  }



  showMsisdns(row: Pdv) {
    if (!row.idPdvMaster) {
      Swal.fire('Erreur', 'ID du PDV introuvable.', 'error');
      return;
    }

    this.pdvService.listMsisdns(row.idPdvMaster).subscribe({
      next: (msisdns) => {
        if (msisdns.length === 0) {
          Swal.fire('Aucun MSISDN', 'Ce PDV n\'a aucun MSISDN associé.', 'info');
          return;
        }

        const htmlContent = `<ul style="text-align:left;">${msisdns.map(m => `<li>${m}</li>`).join('')}</ul>`;

        Swal.fire({
          title: `MSISDN liés`,
          html: htmlContent,
          icon: 'info',
          width: 500
        });
      },
      error: () => {
        Swal.fire('Erreur', 'Impossible de récupérer les MSISDN.', 'error');
      }
    });
  }


  openRemoveMsisdnModal(row: PdvGrouped) {
    if (!row.idPdvMaster) {
      Swal.fire('Erreur', 'ID du PDV introuvable.', 'error');
      return;
    }
    if (row.msisdns.length === 0) {
      Swal.fire('Aucun MSISDN', 'Ce PDV n\'a aucun MSISDN associé.', 'info');
      return;
    }

    this.msisdnsToRemove = row.msisdns;
    this.selectedMsisdnToRemove = '';
    this.currentRemoveMsisdnPdvId = row.idPdvMaster;
    this.displayRemoveMsisdnModal = true;
  }

  confirmRemoveMsisdn() {
    if (!this.selectedMsisdnToRemove || !this.currentRemoveMsisdnPdvId) {
      Swal.fire('Erreur', 'Veuillez sélectionner un MSISDN.', 'error');
      return;
    }

    this.pdvService.removeMsisdn(this.currentRemoveMsisdnPdvId, this.selectedMsisdnToRemove).subscribe({
      next: () => {
        Swal.fire({
          title: 'Supprimé',
          text: `Le MSISDN ${this.selectedMsisdnToRemove} a été supprimé.`,
          icon: 'success',
          confirmButtonColor: '#dc3545'
        });
        this.loadPdvs();
        this.displayRemoveMsisdnModal = false;
      },
      error: () => {
        Swal.fire({
          title: 'Erreur',
          text: 'La suppression a échoué.',
          icon: 'error',
          confirmButtonColor: '#dc3545'
        });
      }
    });
  }



  handleFileInput(event: Event) {
    const input = event.target as HTMLInputElement;
    if (!input.files || input.files.length === 0) {
      return;
    }

    const file = input.files[0];

    Swal.fire({
      title: 'Confirmer l\'import',
      text: `Voulez-vous importer le fichier "${file.name}" ?`,
      icon: 'question',
      showCancelButton: true,
      confirmButtonColor: '#dc3545',
      cancelButtonColor: '#6c757d',
      confirmButtonText: 'Oui, importer',
      cancelButtonText: 'Annuler'
    }).then(result => {
      if (result.isConfirmed) {
        this.uploadExcel(file);
      }
    });


  }


  uploadExcel(file: File) {
    this.pdvService.importExcel(file).subscribe({
      next: () => {
        Swal.fire('Succès', 'Le fichier a été importé avec succès.', 'success');
        this.loadPdvs(); // Refresh la liste
      },
      error: (err) => {
        console.error(err);
        Swal.fire('Erreur', 'Erreur lors de l\'import du fichier.', 'error');
      }
    });
  }


}
