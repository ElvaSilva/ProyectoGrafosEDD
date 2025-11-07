/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto.pkg1;

final class DiccionarioStringInt {
    private static class Nodo {
        String clave;
        int valor;
        Nodo sig;
        Nodo(String c, int v){ this.clave=c; this.valor=v; }
    }

    private Nodo cabeza;

    public void put(String clave, int valor) {
        if (cabeza == null) { cabeza = new Nodo(clave, valor); return; }
        Nodo p = cabeza;
        while (true) {
            if (p.clave.equals(clave)) { p.valor = valor; return; }
            if (p.sig == null) break;
            p = p.sig;
        }
        p.sig = new Nodo(clave, valor);
    }

    public boolean containsKey(String clave) {
        for (Nodo p=cabeza; p!=null; p=p.sig)
            if (p.clave.equals(clave)) return true;
        return false;
    }

    // Devuelve null si no existe para poder “simular” el get() que puede ser nulo
    public Integer get(String clave) {
        for (Nodo p=cabeza; p!=null; p=p.sig)
            if (p.clave.equals(clave)) return Integer.valueOf(p.valor);
        return null;
    }

    public void clear(){ cabeza = null; }
}
