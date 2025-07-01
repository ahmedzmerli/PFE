import { Component, OnInit } from '@angular/core';
import { BlacklistService } from 'src/app/services/blacklist.service';
import { Blacklist } from 'src/app/models/blacklist.model';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-blacklist-management',
  templateUrl: './blacklist-management.component.html',
  styleUrls: ['./blacklist-management.component.scss']
})
export class BlacklistManagementComponent implements OnInit {
  blacklist: Blacklist[] = [];
  filtered: Blacklist[] = [];

  searchMsisdn: string = '';
  selectedStatus = '';
  selectedTypeClient = '';
  selectedMotif = '';

  sortField: 'dureeBlacklist' | 'dateAction' | '' = '';
  sortDirection: 'asc' | 'desc' = 'asc';

  statusOptions = [
    { label: 'BLACKLISTED', value: 'BLACKLISTED' },
    { label: 'WHITELISTED', value: 'WHITELISTED' },
    { label: 'BL', value: 'BL' },
    { label: 'WL', value: 'WL' }
  ];
  typeClientOptions: any[] = [];

  showModal = false;

  cols = [
    { field: 'msisdn', header: 'MSISDN' },
    { field: 'segment', header: 'Segment' },
    { field: 'dateDebut', header: 'Début' },
    { field: 'dateFin', header: 'Fin' },
    { field: 'motif', header: 'Motif' },
    { field: 'offre', header: 'Offre' },
    { field: 'statut', header: 'Statut' },
    { field: 'username', header: 'Utilisateur' },
    { field: 'typeClient', header: 'Type Client' },
    { field: 'dureeBlacklist', header: 'Durée' },
    { field: 'dateAction', header: 'Date action' },
  ];
  selectedColumns = [...this.cols];

  resetColumns(): void {
    this.selectedColumns = [...this.cols];
  }

  getColumnIcon(field: string): string {
    const icons: any = {
      msisdn: 'pi pi-phone',
      segment: 'pi pi-sitemap',
      dateDebut: 'pi pi-calendar-plus',
      dateFin: 'pi pi-calendar-minus',
      motif: 'pi pi-info-circle',
      offre: 'pi pi-gift',
      statut: 'pi pi-flag',
      username: 'pi pi-user',
      typeClient: 'pi pi-users',
      dureeBlacklist: 'pi pi-clock',
      dateAction: 'pi pi-history'
    };
    return icons[field] || '';
  }

  formatColumnData(b: any, field: string): any {
    if (field === 'statut') {
      return b[field];
    }
    if (field === 'dateDebut' || field === 'dateFin') {
      return b[field] ? new Date(b[field]).toLocaleDateString() : '';
    }
    if (field === 'dateAction') {
      return b[field] ? new Date(b[field]).toLocaleString() : '';
    }
    return b[field];
  }

  constructor(private blacklistService: BlacklistService) {}

  ngOnInit(): void {
    this.loadBlacklist();
  }

  loadBlacklist(): void {
    this.blacklistService.getAll().subscribe(data => {
      this.blacklist = data;
      this.filtered = data;
      this.initDropdowns();
    });
  }

  initDropdowns(): void {
    const types = Array.from(new Set(this.blacklist.map(b => b.typeClient).filter(Boolean)));
    const motifs = Array.from(new Set(this.blacklist.map(b => b.motif).filter(Boolean)));

    this.typeClientOptions = types.map(t => ({ label: t, value: t }));
    this.motifOptions = motifs.map(m => ({ label: m, value: m }));
  }

  applyFilter(): void {
    const term = this.searchMsisdn.trim().toLowerCase();

    this.filtered = this.blacklist
      .filter(b =>
        (!term || b.msisdn.toLowerCase().includes(term)) &&
        (!this.selectedStatus || b.statut === this.selectedStatus) &&
        (!this.selectedTypeClient || b.typeClient === this.selectedTypeClient) &&
        (!this.selectedMotif || b.motif === this.selectedMotif)
      );

    if (this.sortField) {
      this.filtered.sort((a: any, b: any) => {
        const valA = a[this.sortField];
        const valB = b[this.sortField];
        if (valA < valB) return this.sortDirection === 'asc' ? -1 : 1;
        if (valA > valB) return this.sortDirection === 'asc' ? 1 : -1;
        return 0;
      });
    }
  }

  resetFilters(): void {
    this.searchMsisdn = '';
    this.selectedStatus = '';
    this.selectedTypeClient = '';
    this.selectedMotif = '';
    this.sortField = '';
    this.sortDirection = 'asc';
    this.applyFilter();
  }

  sortBy(field: 'dureeBlacklist' | 'dateAction') {
    if (this.sortField === field) {
      this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
    } else {
      this.sortField = field;
      this.sortDirection = 'asc';
    }
    this.applyFilter();
  }

  toggle(entry: Blacklist): void {
    if (entry.statut === 'BL') {
      // On construit un vrai BlacklistRequest, même si motif/duree sont arbitraires pour la whitelist
      const dto = {
        msisdn: entry.msisdn,
        motif: entry.motif || 'Auto-whitelist',
        typeClient: entry.typeClient || 'B2C',
        duree: 0 // important, même si ignoré côté back
      };
      this.blacklistService.whitelist(dto).subscribe({
        next: () => {
          Swal.fire('Succès', 'Le statut a été modifié vers WHITELISTED.', 'success');
          this.loadBlacklist();
        },
        error: (err) => {
          console.error('Erreur API:', err);
          Swal.fire('Erreur', 'Impossible de whitelister ce MSISDN.', 'error');
        }
      });
    } else {
      // Affiche le modal de blacklist avec msisdn déjà rempli
      this.form.msisdn = entry.msisdn;
      this.showModal = true;
    }
  }


  confirmWhitelist(msisdn: string): void {
    Swal.fire({
      title: 'Confirmation',
      text: 'Êtes-vous sûr de vouloir whitelister ce MSISDN ?',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Oui',
      cancelButtonText: 'Non'
    }).then((result) => {
      if (result.isConfirmed) {
        this.whitelist(msisdn);
      }
    });
  }

  whitelist(msisdn: string): void {
    const dto = {
      msisdn: msisdn,
      typeClient: 'B2C' // À adapter selon votre logique
    };

    this.blacklistService.whitelist(dto).subscribe({
      next: () => {
        Swal.fire('Succès', 'MSISDN whitelisté avec succès.', 'success');
        this.loadBlacklist();
      },
      error: (err) => {
        console.error('Erreur API:', err);
        Swal.fire('Erreur', 'Impossible de whitelister le MSISDN.', 'error');
      }
    });
  }



  form = {
    msisdn: '',
    motif: '',
    duree: null,
    typeClient: '',
    segment: '',
    offre: ''
  };

  dureeOptions = [
    { label: '1 jour', value: 1 },
    { label: '3 jours', value: 3 },
    { label: '7 jours', value: 7 },
    { label: '30 jours', value: 30 }
  ];

  openModal(): void {
    this.showModal = true;
  }

  resetModal(): void {
    this.form = { msisdn: '', motif: '', duree: null, typeClient: '', segment: '', offre: '' };
  }

  createBlacklist(): void {
    if (!this.form.msisdn || !this.form.motif || this.form.duree === null) {
      Swal.fire('Erreur', 'Tous les champs sont obligatoires.', 'error');
      return;
    }

    const entry = this.blacklist.find(b => b.msisdn === this.form.msisdn);
    if (entry) {
      this.form.typeClient = entry.typeClient || 'N/A';
      this.form.segment = entry.segment || 'N/A';
      this.form.offre = entry.offre || 'N/A';
    }

    const dto = {
      msisdn: this.form.msisdn,
      motif: this.form.motif,
      // typeClient: this.form.typeClient,
      // segment: this.form.segment,
      // offre: this.form.offre,
      duree: this.form.duree
    };

    this.blacklistService.create(dto).subscribe({
      next: () => {
        Swal.fire('Succès', 'Blacklist créée.', 'success');
        this.loadBlacklist();
        this.showModal = false;
        this.resetModal();
      },
      error: (err) => {
        console.error('Erreur API:', err);
        Swal.fire('Erreur', 'Impossible de créer la blacklist.', 'error');
      }
    });
  }

  motifOptions = [
    { label: 'Fraud', value: 'Fraud' },
    { label: 'Spam', value: 'Spam' },
    { label: 'Abus', value: 'Abus' },
    { label: 'Autre', value: 'Autre' }
  ];

  addDays(date: Date, days: number): Date {
    const result = new Date(date);
    result.setDate(result.getDate() + days);
    return result;
  }
}
