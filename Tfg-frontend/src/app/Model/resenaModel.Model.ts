import { Producto } from "./productoModel.model";


export interface Resena {
  idResena?: number;
  titulo: string;
  comentario: string;
  puntuacion: number;
  fecha: string | Date;
  usuario: any; 
  producto: Producto;
}