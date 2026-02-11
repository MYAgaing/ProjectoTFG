import { Categoria } from "./categoriaModel.model";

export interface Producto {
  idProducto: number;
  nombre: string;
  descripcion: string;
  marca: string;
  fechaLanzamiento: string;
  categoria: Categoria;
}
