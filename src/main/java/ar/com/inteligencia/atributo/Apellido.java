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

    public static Apellido fromChromosome(Chromosome<BitGene> chromosome) {
        int gene0 = chromosome.getGene(2).getBit() ? 1 : 0;
        int gene1 = chromosome.getGene(3).getBit() ? 1 : 0;
        int genes = gene0 | (gene1 << 1);
        return Arrays
                .stream(values())
                .filter(apellido -> apellido.value == genes)
                .findFirst()
                .get();
    }
}
