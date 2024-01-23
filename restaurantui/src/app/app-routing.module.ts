import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MainComponent } from "./pages/main/main.component";
import { SessionspageComponent } from './pages/sessionspage/sessionspage.component';
import { CurrentsessionComponent } from './pages/currentsession/currentsession.component';


const routes: Routes = [
  {
    path: '', component: MainComponent
  },
  {
    path: 'sessions', component: SessionspageComponent
  },
  {
    path: 'sessions/:id', component: CurrentsessionComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
