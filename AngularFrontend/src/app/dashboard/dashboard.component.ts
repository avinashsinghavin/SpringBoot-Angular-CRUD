import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Server } from '../server.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
})
export class DashboardComponent implements OnInit {
  
  tableData: any;
  headElements = ['ID', 'Name', 'Email', 'Mobile', 'State', 'Gender', 'Skills', 'EDIT'];

  elements: any = [
    {id: 1, first: 'Mark', last: 'Otto', handle: '@mdo'},
    {id: 2, first: 'Jacob', last: 'Thornton', handle: '@fat'},
    {id: 3, first: 'Larry', last: 'the Bird', handle: '@twitter'},
  ];

  constructor(private router: Router, private server: Server) {}

  ngOnInit(): void {
    this.server.fetchAllUser().subscribe((response) => {
      this.tableData = response;
    });
  }

  edit(index: number) {
    localStorage.setItem('data', JSON.stringify(this.tableData[index]));
    this.router.navigate(['']);
  }
}
