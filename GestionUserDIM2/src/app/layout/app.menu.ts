import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MenuItem } from 'primeng/api';
import { AppMenuitem } from './app.menuitem';

@Component({
  selector: 'app-menu',
  standalone: true,
  imports: [CommonModule, AppMenuitem, RouterModule],
  template: `
    <ul class="layout-menu">
      <ng-container *ngFor="let item of model; let i = index">
        <li *ngIf="!item.separator && item.visible !== false"
            app-menuitem [item]="item" [index]="i" [root]="true"></li>
        <li *ngIf="item.separator" class="menu-separator"></li>
      </ng-container>
    </ul>
  `
})
export class AppMenu implements OnInit {
  model: MenuItem[] = [];

  ngOnInit(): void {
    this.updateMenuModel();

    // 🟡 Recharger le menu si les permissions changent (ex: après login)
    window.addEventListener('permissions-updated', () => {
      this.updateMenuModel();
    });
  }

  updateMenuModel(): void {
    const permissions = JSON.parse(localStorage.getItem('permissions') || '[]');
    console.log('🔐 Permissions détectées :', permissions);

    this.model = [
      {
        // label: 'Admin',
        items: [
          {
            label: 'Utilisateurs',
            icon: 'pi pi-fw pi-users',
            routerLink: ['/users'],
            visible: permissions.includes('users.read')
          },
          {
            label: 'Listes de permissions',
            icon: 'pi pi-fw pi-list',
            routerLink: ['/permission-lists'],
            visible: permissions.includes('permissionlists.read')
          },
          {
            label: 'Rôles',
            icon: 'pi pi-fw pi-id-card',
            routerLink: ['/liste-roles'],
            visible: permissions.includes('roles.read')
          },
          // {
          //   label: 'Blacklist',
          //   icon: 'pi pi-fw pi-ban',
          //   routerLink: ['/blacklist'],
          //   visible: permissions.includes('blacklist.read')
          // },
          //
          // {
          //   label: 'Historique',
          //   icon: 'pi pi-history',
          //   routerLink: ['/historique'],
          //   visible: permissions.includes('blhistory.read')
          // },
          {
            label: 'Blacklist',
            icon: 'pi pi-fw pi-ban',
            visible:
              permissions.includes('blacklist.read') ||
              permissions.includes('blhistory.read'),
            items: [
              {
                label: 'Gestion Blacklist',
                icon: 'pi pi-fw pi-ban',
                routerLink: ['/blacklist'],
                visible: permissions.includes('blacklist.read')
              },
              {
                label: 'Historique',
                icon: 'pi pi-fw pi-history',
                routerLink: ['/historique'],
                visible: permissions.includes('blhistory.read')
              }
            ]
          },

          {
            label: 'Dashboard',
            icon: 'pi pi-history',
            routerLink: ['/dashboard'],
            visible: permissions.includes('dashboard.read')
          },
          {
            label: 'Points de vente',
            icon: 'pi pi-history',
            routerLink: ['/pdv'],
            visible: permissions.includes('pdv.read')
          },
          {
            label: 'Historique points de vente',
            icon: 'pi pi-history',
            routerLink: ['/pdvhistory'],
            visible: permissions.includes('pdvhistory.read')
          }
        ]
      }
    ];

    console.log('🧭 Menu généré :', this.model);
  }
}
