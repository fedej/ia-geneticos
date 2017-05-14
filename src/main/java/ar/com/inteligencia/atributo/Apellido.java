package ar.com.inteligencia.atributo;

import org.jenetics.BitGene;
import org.jenetics.Chromosome;

import java.util.Arrays;

/**
 * Created by fede on 16/04/17.
 */
public enum Apellido {
    ABAD(0b00), VILLAR(0b01), CERVERA(0b10), LAMATA(0b11);

    private final int value;

    Apellido(final int value) {
        this.value = value;
    }

    public static Apellido fromChromosome(boolean[] atributos) {
        int gene0 = atributos[0] ? 1 : 0;
        int gene1 = atributos[1] ? 1 : 0;
        int genes = gene0 | (gene1 << 1);
        return Arrays
                .stream(values())
                .filter(apellido -> apellido.value == genes)
                .findFirst()
                .get();
    }
}
