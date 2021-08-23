import { Component, Input, OnInit } from '@angular/core';
import {
  FormArray,
  FormControl,
  FormGroup,
  Validators,
  FormBuilder,
} from '@angular/forms';
import { Router } from '@angular/router';
import { Server } from '../server.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit {
  genders = ['Male', 'Female'];
  stateList: any;
  form: FormGroup | any;
  checkboxArray = [
    'C++',
    'Java',
    'Python',
    'JavaScript',
    'PHP',
    'SQL',
    'NoSQL',
    'Cloud',
  ];

  skillsList = {
    'C++': false,
    'Java': false,
    'Python': false,
    'JavaScript': false,
    'PHP': false,
    'SQL': false,
    'NoSQL': false,
    'Cloud': false
  }

  fileUpload: any = null;

  constructor(
    private router: Router,
    private fb: FormBuilder,
    private server: Server
  ) {
    this.form = fb.group({
      id: new FormControl(''),
      name: new FormControl('', [Validators.required]),
      mobile: new FormControl('', [Validators.required]),
      email: new FormControl('', [Validators.required, Validators.email]),
      gender: new FormControl('', [Validators.required]),
      state: new FormControl(this.stateList, [Validators.required]),
      skills: this.fb.array([]),
      photoPath: new FormControl('', [Validators.required]),
    });
  }

  ngOnInit(): void {
    this.getState();
    let value = localStorage.getItem('data');
    if (value) {
      const data = JSON.parse(value);
      const skillArray = data['skills'].split(',');
      this.form = this.fb.group({
        id: new FormControl(data.id),
        name: new FormControl(data.name, [Validators.required]),
        mobile: new FormControl(data.mobile, [Validators.required]),
        email: new FormControl(data.email, [
          Validators.required,
          Validators.email,
        ]),
        gender: new FormControl(data.gender, [Validators.required]),
        state: new FormControl('', [Validators.required]),
        skills: this.fb.array([]),
        photoPath: new FormControl(data.photoPath, [Validators.required]),
      });
      this.form.controls['state'].setValue(data.state, { onlySelf: true });
      this.addCheck(skillArray);
    }
  }

  getState() {
    this.server.fetchStates().subscribe((response1) => {
      this.stateList = response1;
    });
  }

  addCheck(data: any) {
    const state: FormArray = this.form.get('skills') as FormArray;
    for(let i = 0; i < data.length; i++) {
      state.push(new FormControl(data[i]));
      // @ts-ignore
      this.skillsList[data[i]] = true;
    }
  }

  onCheckboxChange(e: any) {
    const state: FormArray = this.form.get('skills') as FormArray;
    if (e.target.checked) {
      state.push(new FormControl(e.target.value));
    } else {
      let i: number = 0;
      state.controls.forEach((item) => {
        if (item.value == e.target.value) {
          state.removeAt(i);
          return;
        }
        i++;
      });
    }
  }

  get f() {
    return this.form.controls;
  }

  get state() {
    if (typeof this.form.value.state == 'string') return false;
    return true;
  }

  get skills() {
    if (this.form.value.skills.length > 0) {
      return false;
    } else return true;
  }

  onSubmit() {
    this.form.markAllAsTouched();
    this.state;
    this.skills;
    if (this.form.valid) {
      let dic = this.form.value;
      dic.skills = dic.skills.toString();

      let value = localStorage.getItem('data');
      if (value) {
        this.server.editData(dic).subscribe((response) => {
          let data = JSON.parse(JSON.stringify(response));
          if (data['Message'] === 'Data Successfully Uploaded') {
            if (this.fileUpload === null) {
              alert(data['Message']);
              localStorage.clear();
              this.router.navigate(['dashboard']);
            } else this.uploadFile(data['value']['id']);
          } else if (data['valid'] === false) {
            alert('Invalid Email | Password');
          } else {
            alert(data['Message']);
          }
        });
      } else {
        this.server.uploadData(dic).subscribe((response) => {
          let data = JSON.parse(JSON.stringify(response));
          if (data['Message'] === 'Data Inserted') {
            this.uploadFile(data['value']['id']);
          } else if (data['valid'] === false) {
            alert('Invalid Email | Password');
          } else {
            alert(data['Message']);
          }
        });
      }
    }
  }

  setFile(files: any) {
    if (files.target.files) {
      this.form.controls['photoPath'].setValue(files.target.files[0].name);
      this.fileUpload = files.target.files[0];
    }
  }

  uploadFile(id: any) {
    const formData = new FormData();
    formData.append('id', id);
    formData.append('photo', this.fileUpload);
    this.server.uploadFile(formData).subscribe((response) => {
      let data = JSON.parse(JSON.stringify(response));
      if (data['Message'] === 'Success') {
        alert('Data Uploaded Successfully');
        localStorage.clear();
        this.router.navigate(['dashboard']);
      } else {
        alert(data['Message']);
      }
    });
  }

  getCheck(data: any):boolean {
    // @ts-ignore
    return this.skillsList[data];
  }
}
