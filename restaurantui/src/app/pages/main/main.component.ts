import { Component } from '@angular/core';
import { Router } from '@angular/router'

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class MainComponent {

  constructor(private router: Router) {

  }


  goToSessionsPage() {
    // add validation to make sure user type name
    this.router.navigate(['/sessions'])
  }


}
