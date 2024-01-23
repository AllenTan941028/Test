import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ApiService } from 'src/app/services/api.service';
import { interval, Subscription } from 'rxjs';
@Component({
  selector: 'app-currentsession',
  templateUrl: './currentsession.component.html',
  styleUrls: ['./currentsession.component.css']
})
export class CurrentsessionComponent {
  private fetchDataSubscription: Subscription = new Subscription;
  showError: boolean = true;
  restaurantList: any;
  sessionId: any = ""
  newRestaurant: any = ""
  username: any = ""
  selectedRestaurant: string = "Voting...."

  constructor(public apiService: ApiService, private route: ActivatedRoute, private router: Router) {
  }

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      console.log(params);
      const p = params.get("state");
      const id = params.get("id");
      if(id){
        this.sessionId = id;
      }
      if(p){
        let obj = JSON.parse(params.get("state") || '')
        console.log(obj)
        if(obj.data !== ''){
          this.restaurantList = JSON.parse(obj.data)
        }
        this.sessionId = obj.sessionId
        this.username = obj.username
        console.log(this.sessionId)
      }
    });
    //get restaurant list when user access the page
    this.refreshRestaurantLst();

  }

  refreshRestaurantLst(){
    this.apiService.getAllRestaurant(this.sessionId).subscribe({
      next: (data)=>{
        this.restaurantList = JSON.parse(data)
        this.showError = false;
        this.startInterval()
      },
      error: (error) =>{
        this.showError = true;
        this.showAlertAndRedirect();
        throw new Error('An error occurred in ngOnInit.');
      }
    });
  }

  startInterval(){
    this.fetchDataSubscription = interval(5000).subscribe(() =>{
      this.apiService.getSelectedRestaurant(this.sessionId).subscribe({
        next: (data) =>{
          console.log(data);
          if(data !== ''){
            this.selectedRestaurant = data
          }else{
            this.selectedRestaurant = "Voting....";
          }
        },
        error: (error) =>{
          console.log("Refresh selected restaurant error: " + error)
        }
      })
    })
  }

  submit() {
    this.apiService.submitRestaurant(this.sessionId, this.newRestaurant).subscribe(data => {
      console.log(data)
      this.restaurantList = JSON.parse(data)
      this.newRestaurant = "";
    })
  }


  endSession() {
    if(this.username){
      // api call to end session with two params: userId + sessionId
      this.apiService.endSession(this.sessionId, this.username).subscribe({
        next: (data) =>{
          this.showSelectedRestaurantAndRedirect(data)
        },
        error: (error) =>{
          alert('Only host can end this session.')
        }
      })
    }else{
      //pop up modal
      this.username = window.prompt("Enter host name: ", "");
    }
  }

  showAlertAndRedirect() {
    const userConfirmed = window.confirm('Session does not exists.');
    if(userConfirmed){
      this.router.navigate(['/sessions'], { replaceUrl: true});
    }
  }

  showSelectedRestaurantAndRedirect(data: any){
    const userConfirmed = window.confirm('Session has ended. The final selected restaurant is : ' + data)
        if(userConfirmed){
          this.router.navigate(['/sessions'], { replaceUrl: true});
        }
  }
  
  ngOnDestroy() {
    this.fetchDataSubscription.unsubscribe();
  }
}
