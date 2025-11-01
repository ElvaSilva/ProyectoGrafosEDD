/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto.pkg1;

/**
 *
 * @author USER
 */
public class NodoGrafo {
    public String usuario;
    public ListaAdyacencia minilista;
    public NodoGrafo pnext;
    
    public NodoGrafo(String info){
        usuario = info;
        minilista = new ListaAdyacencia();
        pnext = null;
    }
}
