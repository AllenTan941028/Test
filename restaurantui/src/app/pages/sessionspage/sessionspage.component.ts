import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ApiService } from 'src/app/services/api.service';

@Component({
  selector: 'app-sessionspage',
  templateUrl: './sessionspage.component.html',
  styleUrls: ['./sessionspage.component.css']
})
export class SessionspageComponent {

  constructor(public apiService: ApiService, private router: Router) {
  }
  username: any = "";
  sessionId: any = "";

  ngOnInit() { }

  startSession() {
    // call api to start session
    if(this.username){
      this.apiService.startSession(this.username).subscribe(data =>{
        let obj = {
          data: '',
          sessionId: data,
          username: this.username
        }
        this.router.navigate(["/sessions/" + data, {
          state: JSON.stringify(obj),
        }])
      })
    }else{
      //pop up modal
      this.username = window.prompt("Enter host name: ", "");
    }
  }


  joinSession() {
    if(this.sessionId){
      this.apiService.joinSession(this.sessionId).subscribe({
        next: (data) =>{
          let obj = {
            data: data,
            sessionId: this.sessionId,
            username: this.username
          }
          this.router.navigate(["/sessions/" + this.sessionId, {
            state: JSON.stringify(obj),
          }])
        },
        error: (error) =>{
          alert('Session does not exists.');
          this.sessionId = ''
        }})
      }
    }
}
