import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

export interface Column {
  label: string;
  key: string;
  isBadge?: boolean;
}

@Component({
  selector: 'app-management-table',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './data-table.html',
  styleUrl: './data-table.css'
})
export class DataTableComponent {
  @Input() title: string = '';
  @Input() columns: Column[] = [];
  @Input() data: any[] = [];
  
  @Output() onAdd = new EventEmitter<void>();
  @Output() onEdit = new EventEmitter<any>();
  @Output() onDelete = new EventEmitter<any>();

  searchTerm: string = '';

  get filteredData() {
    const term = this.searchTerm.toLowerCase().trim();
    if (!term) return this.data;
    
    return this.data.filter(item => 
      Object.values(item).some(val => 
        String(val).toLowerCase().includes(term)
      )
    );
  }
}