import { Component, OnInit } from '@angular/core';
import { DashboardService } from 'src/app/services/dashboard.service';
import { Dashboard } from 'src/app/models/dashboard.model';
import Swal from "sweetalert2";

@Component({
  selector: 'app-dashboard-management',
  templateUrl: './dashboard-management.component.html',
  styleUrls: ['./dashboard-management.component.scss']
})
export class DashboardManagementComponent implements OnInit {
  msisdn = '';
  hotline = '';
  startDate?: Date;
  endDate?: Date;
  dateError = false;
  requiredError = false;

  loading = false;

  results: Dashboard[] = [];
  filteredResults: Dashboard[] = [];

  cols = [
    { field: 'callid', header: 'CallID' },
    { field: 'hotline', header: 'Hotline' },
    { field: 'num_CLIENT', header: 'MSISDN' },
    { field: 'time_IN_QUEUE', header: 'Temps File' },
    { field: 'file_ATT', header: 'File Att' },
    { field: 'date_HEURS', header: 'Date Heure' },
    { field: 'status', header: 'Statut' },
    { field: 'segment', header: 'Segment' }
  ];
  selectedColumns = [...this.cols];

  statusOptions = [
    { label: 'Tous', value: '' },
    { label: 'appel routé répondu', value: 'appel routé répondu' },
    { label: 'appel routé et non répondu', value: 'appel routé et non répondu' },
    { label: 'IVR', value: 'IVR' }
  ];
  selectedStatus = '';

  constructor(private dashboardService: DashboardService) {}

  ngOnInit(): void {}

  search(): void {
    this.dateError = false;
    this.requiredError = false;

    if (!this.msisdn.trim() || !this.hotline.trim()) {
      this.requiredError = true;
      return;
    }

    if (this.startDate && this.endDate && this.startDate > this.endDate) {
      this.dateError = true;
      return;
    }

    this.loading = true; // 🚀 Start spinner

    this.dashboardService.searchDashboard(
      this.msisdn,
      this.hotline,
      this.startDate,
      this.endDate
    ).subscribe({
      next: (data) => {
        this.results = data;
        this.applyFilter();
        this.loading = false;

        if (data.length === 0) {
          Swal.fire({
            icon: 'info',
            title: 'Aucun résultat',
            text: 'Aucun appel trouvé pour les filtres sélectionnés.',
            confirmButtonColor: '#d33'
          });
        }
      },
      error: (err) => {
        this.loading = false;
        console.error(err);
        Swal.fire({
          icon: 'error',
          title: 'Erreur de chargement',
          text: 'Impossible de récupérer les données du serveur.',
          confirmButtonColor: '#d33'
        });
      }
    });


  }

  applyFilter(): void {
    if (this.selectedStatus) {
      this.filteredResults = this.results.filter(d => d.status === this.selectedStatus);
    } else {
      this.filteredResults = [...this.results];
    }
  }

  resetFilters(): void {
    this.msisdn = '';
    this.hotline = '';
    this.startDate = undefined;
    this.endDate = undefined;
    this.selectedStatus = '';
    this.filteredResults = [];
    this.results = [];
    this.dateError = false;
    this.requiredError = false;
  }

  resetColumns(): void {
    this.selectedColumns = [...this.cols];
  }


  exportToCSV(): void {
    const rows = this.filteredResults;
    if (!rows.length) {
      Swal.fire({
        icon: 'info',
        title: 'Aucun appel à exporter',
        confirmButtonColor: '#d33'
      });
      return;
    }

    const headers = this.selectedColumns.map(col => col.header);
    const fields = this.selectedColumns.map(col => col.field);

    const csv = [
      headers.join(','),
      ...rows.map(row =>
        fields.map(field => `"${(row as any)[field] ?? ''}"`).join(',')
      )
    ].join('\n');

    const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' });
    const link = document.createElement('a');
    const now = new Date();
    const timestamp = now.toISOString().slice(0, 19).replace(/[:T]/g, '-');
    link.setAttribute('href', URL.createObjectURL(blob));
    link.setAttribute('download', `export_appels_${timestamp}.csv`);
    link.click();
  }

}
