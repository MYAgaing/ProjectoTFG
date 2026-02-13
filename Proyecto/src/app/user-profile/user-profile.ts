import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { AuthServiceTs } from '../Auth/ServiceAuth/auth.service';

@Component({
  selector: 'app-user-profile',
  imports: [CommonModule],
  templateUrl: './user-profile.html',
  styleUrl: './user-profile.css',
})
export class UserProfile implements OnInit {

  user: any;

  constructor(private auth: AuthServiceTs) { }

  ngOnInit() {
    this.auth.getUserProfile()?.subscribe(data => {
      this.user = data;
    });
  }

}
