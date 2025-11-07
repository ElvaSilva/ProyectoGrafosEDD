/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto.pkg1;

final class ListaCadena {
    static class Nodo {
        String val;
        Nodo sig;
        Nodo(String v){ this.val = v; }
    }
    private Nodo primero, ultimo;

    public boolean esVacio(){ return primero == null; }

    public void agregar(String s){
        Nodo n = new Nodo(s);
        if (primero == null){ primero = ultimo = n; }
        else { ultimo.sig = n; ultimo = n; }
    }

    public String join(String sep){
        StringBuilder sb = new StringBuilder();
        for (Nodo p=primero; p!=null; p=p.sig){
            if (p != primero) sb.append(sep);
            sb.append(p.val);
        }
        return sb.toString();
    }
}

