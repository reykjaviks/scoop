import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { NeighbourhoodVenuesComponent } from './neighbourhood-venues/neighbourhood-venues.component';

const routes: Routes = [
  { path: '', component: NeighbourhoodVenuesComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DiscoverRoutingModule { }
