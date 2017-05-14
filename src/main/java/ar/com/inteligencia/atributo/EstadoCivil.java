package ar.com.inteligencia.atributo;

import org.jenetics.BitGene;
import org.jenetics.Chromosome;

import java.util.Arrays;

/**
 * Created by fede on 16/04/17.
 */
public enum EstadoCivil {
    VIUDO(0b00), CASADO(0b01), DIVORCIADO(0b10), SOLTERO(0b11);

    private final int value;

    EstadoCivil(final int value) {
        this.value = value;
    }

    public static EstadoCivil fromChromosome(boolean[] atributos) {
        int gene0 = atributos[4] ? 1 : 0;
        int gene1 = atributos[5] ? 1 : 0;
        int genes = gene0 | (gene1 << 1);
        return Arrays
                .stream(values())
                .filter(estadoCivil -> estadoCivil.value == genes)
                .findFirst()
                .get();
    }
}
