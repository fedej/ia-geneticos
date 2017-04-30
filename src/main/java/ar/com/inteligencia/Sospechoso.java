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

    public Sospechoso(final Chromosome<BitGene> chromosome) {
        nombre = Nombre.fromChromosome(chromosome);
        apellido = Apellido.fromChromosome(chromosome);
        edad = getEdad(chromosome);
        estadoCivil = EstadoCivil.fromChromosome(chromosome);
        inocente = chromosome.getGene(8).booleanValue();

        acusoAGustavo = chromosome.getGene(9).booleanValue();
        acusoAGustavo2 = chromosome.getGene(10).booleanValue();
        quiereASuMujer = chromosome.getGene(11).booleanValue();
        primosEnParis = chromosome.getGene(12).booleanValue();
        primosEnLondres = chromosome.getGene(13).booleanValue();
        aficionadoAlDomino = chromosome.getGene(14).booleanValue();
        campeonDeBillar = chromosome.getGene(15).booleanValue();
        nacioEnAnioBisiesto = chromosome.getGene(16).booleanValue();
    }

    private Integer getEdad(Chromosome<BitGene> chromosome) {
        int gene0 = getGeneValue(chromosome, 4);
        int gene1 = getGeneValue(chromosome, 5);
        return edades.get(gene0 | (gene1 << 1));
    }

    private static int getGeneValue(Chromosome<BitGene> chromosome, int index) {
        return chromosome.getGene(index).getBit() ? 1 : 0;
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
