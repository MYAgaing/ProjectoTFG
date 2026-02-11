import { CommonModule } from '@angular/common';
import { AfterViewInit, Component, ViewChild, ElementRef } from '@angular/core';

@Component({
  selector: 'app-home',
  imports: [CommonModule],
  templateUrl: './home.html',
  styleUrls: ['./home.css'],
})
export class Home {

  products = [
    {
      name: "iPhone 15 Pro Max",
      rating: 4.8,
      reviews: "15,420",
      img: "https://images.unsplash.com/photo-1695048133142-1a20484d2569"
    },
    {
      name: "Sony WH-1000XM5",
      rating: 4.7,
      reviews: "8,934",
      img: "https://images.unsplash.com/photo-1580894908361-967195033215"
    },
    {
      name: "MacBook Air M3",
      rating: 4.9,
      reviews: "12,567",
      img: "https://images.unsplash.com/photo-1517336714731-489689fd1ca8"
    },
    {
      name: "Apple Watch Series 9",
      rating: 4.6,
      reviews: "9,821",
      img: "https://images.unsplash.com/photo-1516574187841-cb9cc2ca948b"
    }
  ];

  juegos = [
    {
      titulo: "Zelda: Tears of the Kingdom",
      rating: 4.9,
      reviews: "10,000",
      img: "https://images.unsplash.com/photo-1605901309584-818e25960a8f"
    },
    {
      titulo: "Elden Ring",
      rating: 4.9,
      reviews: "10,000",
      img: "https://images.unsplash.com/photo-1612287230202-1ff1d85d1bdf"
    },
    {
      titulo: "Red Dead Redemption 2",
      rating: 4.9,
      reviews: "10,000",
      img: "https://images.unsplash.com/photo-1542751371-adc38448a05e"
    },
    {
      titulo: "Hades",
      rating: 4.9,
      reviews: "10,000",
      img: "https://images.unsplash.com/photo-1550745165-9bc0b252726f"
    },
    {
      titulo: "Minecraft",
      rating: 4.9,
      reviews: "10,000",
      img: "https://images.unsplash.com/photo-1627856013091-fed6e4e30025"
    }
  ];

  libros = [
    {
      titulo: "El Hobbit",
      autor: "J.R.R. Tolkien",
      rating: 4.8,
      img: "https://images.unsplash.com/photo-1544947950-fa07a98d237f"
    },
    {
      titulo: "1984",
      autor: "George Orwell",
      rating: 4.7,
      img: "https://images.unsplash.com/photo-1512820790803-83ca734da794"
    },
    {
      titulo: "Dune",
      autor: "Frank Herbert",
      rating: 4.9,
      img: "https://images.unsplash.com/photo-1495446815901-a7297e633e8d"
    },
    {
      titulo: "Clean Code",
      autor: "Robert C. Martin",
      rating: 4.8,
      img: "https://images.unsplash.com/photo-1517433456452-f9633a875f6f"
    },
    {
      titulo: "Harry Potter y la piedra filosofal",
      autor: "J.K. Rowling",
      rating: 4.9,
      img: "https://images.unsplash.com/photo-1532012197267-da84d127e765"
    }
  ];
}