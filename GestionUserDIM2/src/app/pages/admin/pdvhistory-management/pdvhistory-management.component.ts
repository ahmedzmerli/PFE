import { Component, OnInit } from '@angular/core';
import { PdvService } from '../../../services/pdv.service';
import { PdvHistory } from 'src/app/models/pdvhistory.model';

@Component({
  selector: 'app-pdvhistory-management',
  templateUrl: './pdvhistory-management.component.html',
  styleUrls: ['./pdvhistory-management.component.scss']
})
export class PdvhistoryManagementComponent implements OnInit {
  cols = [
    { field: 'pdvMasterId', header: 'ID PDV' },
    { field: 'username', header: 'Utilisateur' },
    { field: 'actionType', header: 'Action' },
    { field: 'dateAction', header: 'Date action' }
  ];
  selectedColumns = [...this.cols];

  histories: PdvHistory[] = [];
  filteredResults: PdvHistory[] = [];
  pdvMasterId: string = '';

  constructor(private pdvHistoryService: PdvService) {}

  ngOnInit() {
    this.loadHistory();
  }

  loadHistory() {
    this.pdvHistoryService.getHistory().subscribe(data => {
      this.histories = data;
      this.filteredResults = data;
    });
  }

  resetColumns() {
    this.selectedColumns = [...this.cols];
  }

  applyFilter() {
    const search = this.pdvMasterId.trim();
    if (!search) {
      this.filteredResults = [...this.histories];
      return;
    }
    this.filteredResults = this.histories.filter(h =>
      h.pdvMasterId && h.pdvMasterId.toString().includes(search)
    );
  }
}
