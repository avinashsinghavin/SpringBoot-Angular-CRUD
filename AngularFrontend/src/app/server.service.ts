import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
@Injectable({
  providedIn: 'root',
})
export class Server {
  // API url
  adduser = 'http://localhost:8080/addUser';
  addImage = 'http://localhost:8080/saveImage';
  editUser = 'http://localhost:8080/editUser';
  getAllUser = 'http://localhost:8080/getAllUsers';
  getStates = 'http://localhost:8080/getStates';

  constructor(private http: HttpClient) {}

  uploadData(form: any) {
    return this.http.post(this.adduser, form);
  }

  uploadFile(fileToUpload: any) {
    return this.http.post(this.addImage, fileToUpload);
  }

  fetchAllUser() {
      return this.http.get(this.getAllUser);
  }

  editData(form: any) {
    return this.http.post(this.editUser, form);
  }

  fetchStates(){
    return this.http.get(this.getStates);
  }
}
