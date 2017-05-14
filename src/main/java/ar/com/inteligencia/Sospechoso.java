package ar.com.inteligencia;

import ar.com.inteligencia.atributo.Apellido;
import ar.com.inteligencia.atributo.EstadoCivil;
import ar.com.inteligencia.atributo.Nombre;
import lombok.Value;
import org.jenetics.BitGene;
import org.jenetics.Chromosome;

import java.util.HashMap;
import java.util.Map;

@Value
public class Sospechoso {

    private static final Map<Integer, Integer> edades = new HashMap<Integer, Integer>(){{
        put(0b00, 42);
        put(0b01, 44);
        put(0b10, 50);
        put(0b11, 54);
    }};

    private Nombre nombre;
    private Apellido apellido;
    private Integer edad;
    private EstadoCivil estadoCivil;
    private boolean inocente;
    private boolean acusoAGustavo;
    private boolean acusoAGustavo2;
    private boolean quiereASuMujer;
    private boolean primosEnParis;
    private boolean primosEnLondres;
    private boolean aficionadoAlDomino;
    private boolean campeonDeBillar;
    private boolean nacioEnAnioBisiesto;

    public Sospechoso(final Nombre nombre, final boolean[] atributos) {
        this.nombre = nombre;
        apellido = Apellido.fromChromosome(atributos);
        edad = getEdad(atributos);
        estadoCivil = EstadoCivil.fromChromosome(atributos);

        acusoAGustavo = atributos[6];
        acusoAGustavo2 = atributos[7];
        quiereASuMujer = atributos[8];
        primosEnParis = atributos[9];
        primosEnLondres = atributos[10];
        aficionadoAlDomino = atributos[11];
        campeonDeBillar = atributos[12];
        nacioEnAnioBisiesto = atributos[13];

        inocente = atributos[14];
    }

    private Integer getEdad(final boolean[] atributos) {
        int gene0 = atributos[2] ? 1 : 0;
        int gene1 = atributos[3] ? 1 : 0;
        return edades.get(gene0 | (gene1 << 1));
    }

    public String toString() {
        String formated = String.format("%s %s de %d años es %s y esta %s",
                nombre, apellido, edad, inocente ? "inocente" : "culpable", estadoCivil);
        formated += isAcusoAGustavo() ? ", acuso a gustavo" : "";
        formated += isAcusoAGustavo2() ? ", acuso a gustavo2" : "";
        formated += isQuiereASuMujer() ? ", sigue queriendo a su mujer" : "";
        formated += isPrimosEnLondres() ? ", tiene primos en londres" : "";
        formated += isPrimosEnParis() ? ", tiene primos en paris" : "";
        formated += isAficionadoAlDomino() ? ", es aficionado al domino" : "";
        formated += isCampeonDeBillar() ? ", es campeon de billar" : "";
        formated += isNacioEnAnioBisiesto() ? ", nacio en anio bisiesto" : "";
        return formated;
    }
}
