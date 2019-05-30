package Cripto;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 *
 * @author Rodrigo Chavez
 */
public class TareaCurvasElipticas {

    static int p = 30677;
    static int d = 123;
    static int k = 555;
    String FILE_PATH ="/home/netbeans10/Documento";
    //static int hm = 34653;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TareaCurvasElipticas curva = new TareaCurvasElipticas();
        //------------calculas todos los puntos de la curva --------------------
//        int cont = 1;
//        for (int i = 0; i < p; i++) {
//            for (int j = 0; j < p; j++) {
//                if(curva.evaluaCurva(i, j)){
//                    cont++;
//                }
//            }
//        }
//        System.out.println("|G| = " + cont);
//        System.out.println("\n ");
        //----------------------------------------------------------------------
        int hm = 0;
        try {
            hm = curva.SHAfa();
        } catch (Exception e) {
        }
        //---------calcula el orden del punto (x, y)----------------------------
        PuntoRacional punto1 = new PuntoRacional(1090, 18593);
        PuntoRacional punto2 = new PuntoRacional(1090, 18593);
        int orden = 1;
        if (punto2.x != -1) {
            while (true) {
                orden++;
                punto2 = curva.sumaPuntosObjeto(punto1.x, punto1.y, punto2.x, punto2.y);
                if (punto2.x == -1) {
                    break;
                }
//                System.out.println("");
            }
            System.out.println("orden " + orden);
        } else {
            System.out.println("orden " + orden);
        }
        //----------------------------------------------------------------------
        //----------calcula la llave publica  Q=dP -----------------------------
        punto1 = new PuntoRacional(1090, 18593);
        punto2 = new PuntoRacional(1090, 18593);
        for (int i = 1; i < d; i++) {
            punto2 = curva.sumaPuntosObjeto(punto1.x, punto1.y, punto2.x, punto2.y);
        }
        PuntoRacional Q = new PuntoRacional(punto2.x, punto2.y);
        System.out.println("la llave publica Q es (" + punto2.x + "," + punto2.y + ")");
        //----------------------------------------------------------------------
        //-------------algoritmo de firma---------------------------------------
        punto1 = new PuntoRacional(1090, 18593);
        punto2 = new PuntoRacional(1090, 18593);
        for (int i = 1; i < k; i++) {
            punto2 = curva.sumaPuntosObjeto(punto1.x, punto1.y, punto2.x, punto2.y);
        }

        System.out.println("kP = (" + punto2.x + "," + punto2.y + ")");
        int r = (punto2.x) % orden;
        System.out.println("r = " + r);
        int inversok = curva.inversoMultiplicativo(orden, k);
        System.out.println("inverso de k = " + inversok);
        int s = (inversok * (hm + (d * r))) % orden;
        System.out.println("s = " + s);
        //----------------------------------------------------------------------
        //------------------verificacion de firma-------------------------------
        int w = curva.inversoMultiplicativo(orden, s);
        int u1 = (hm * w) % orden;
        int u2 = (r * w) % orden;
        PuntoRacional u1P = new PuntoRacional(0, 0);
        PuntoRacional u2Q = new PuntoRacional(0, 0);
        punto1 = new PuntoRacional(1090, 18593);
        punto2 = new PuntoRacional(1090, 18593);
        for (int i = 1; i < u1; i++) {
            punto2 = curva.sumaPuntosObjeto(punto1.x, punto1.y, punto2.x, punto2.y);
        }
        u1P= new PuntoRacional(punto2.x, punto2.y);
        punto1 = new PuntoRacional(Q.x, Q.y);
        punto2 = new PuntoRacional(Q.x, Q.y);
        for (int i = 1; i < u2; i++) {
            punto2 = curva.sumaPuntosObjeto(punto1.x, punto1.y, punto2.x, punto2.y);
        }
        u2Q= new PuntoRacional(punto2.x, punto2.y);
        punto2 = curva.sumaPuntosObjeto(u1P.x, u1P.y, u2Q.x, u2Q.y);
        int v = punto2.x % orden;
        System.out.println("v = "+v + "  r = "+ r);
        if (v == r){
            System.out.println("firma aceptada");
        }else {
            System.out.println("firma rechazada");
        }
        System.out.println("w =" +w);
        System.out.println("u1= "+u1);
        System.out.println("u2= "+u2);
        System.out.println("u1*P = (" +u1P.x+","+ u1P.y+")");
        System.out.println("u2*Q = (" +u2Q.x+","+ u2Q.y+")");
        //----------------------------------------------------------------------
    }

    public boolean evaluaCurva(int x, int y) {
        boolean salida = false;
        if (Math.pow(y, 2) % p == (Math.pow(x, 3) + x + 1) % p) {
            //if (((y*y) % p) == (((x*x*x)+x+1) % p)) {
            System.out.println("(" + x + "," + y + ")");
            salida = true;
            return salida;
        } else {
            return salida;
        }
    }

    public PuntoRacional sumaPuntosObjeto(int x1, int y1, int x2, int y2) {
        if (x1 == -1) {
            //System.out.println("" + x2 + " " + y2);
            PuntoRacional punto = new PuntoRacional(x2, y2);
            return punto;
        } else if (x2 == -1) {
            //System.out.println("" + x1 + " " + y1);
            PuntoRacional punto = new PuntoRacional(x1, y1);
            return punto;
        }
        if (x1 == x2 && y2 == (p - y1) % p) {
            //System.out.println("(-1,-1)");
            PuntoRacional punto = new PuntoRacional(-1, -1);
            return punto;
        }
        int alpha = 0;
        if (x1 == x2 && y1 == y2) {
            alpha = (((((int) Math.pow(x1, 2)) % p) * (3) % p) + (1) % p) * (inversoMultiplicativo(p, (y1 + y2) % p)) % p;
        } else {
            alpha = ((((y1 + (p - y2)) % p)) * (inversoMultiplicativo(p, (x1 + (p - x2)) % p))) % p;
        }
        int x3;
        x3 = (((int) Math.pow(alpha, 2)) % p + (p - ((x1 + x2) % p))) % p;
        //System.out.println("x= " + x3);
        int y3;
        y3 = (((alpha) * ((x1) + (p - x3) % p) % p) + (p - y1)) % p;
        //System.out.println("y= " + y3);
        PuntoRacional punto = new PuntoRacional(x3, y3);
        return punto;
    }

    /**
     *
     * @param p de Zp
     * @param numero del que se calcula el inverso
     * @return
     */
    public int inversoMultiplicativo(int p, int numero) {
        for (int i = 0; i < p; i++) {
            if ((numero * i) % p == 1) {
                //System.out.println("inverso de " + numero + " es " + i);
                return i;
            } else {

            }
        }
        return -1;
    }
    
    public int SHAfa () throws Exception{
        InputStream is = null;
        int i;
        char c;
        int[] suma = {0, 0};
        int resultado=0;
        try {
            // new input stream created
            is = new FileInputStream(FILE_PATH);

            //System.out.println("Characters printed:");

            // reads till the end of the stream
            while (true) {
                // converts integer to character
                if ((i = is.read()) == -1) {
                    break;
                }
                c = (char) i;
                // prints character       
                //System.out.print(c);
                //System.out.println(c + "   " + i);
                suma[0] = (suma[0] + i) & 0b11111111;
                //System.out.println("");
                //break;

                if ((i = is.read()) == -1) {
                    break;
                }
                c = (char) i;
                // prints character       
                //System.out.println(c + "   " + i);
                suma[1] = (suma[1] + i) & 0b11111111;
                //break;

            }
            suma[0] = (suma[0] * suma[1]) & 0b11111111;
            suma[1] = (suma[1] * suma[0]) & 0b11111111;
            resultado = (((256* suma[1]))  + suma[0] ) ;  //recuerda que el resultado es un entero de 16 bits
            System.out.println(String.format("suma [0] = %x", suma[0]) + " " + String.format("suma [1] = %x", suma[1])   + " " + String.format("hash = %x", resultado));
            System.out.println("hash en decimal = "+ resultado);
        } catch (Exception e) {
            // if any I/O error occurs
            e.printStackTrace();
        } finally {
            // releases system resources associated with this stream
            if (is != null) {
                is.close();
            }
        }
        return resultado;
    }
}
