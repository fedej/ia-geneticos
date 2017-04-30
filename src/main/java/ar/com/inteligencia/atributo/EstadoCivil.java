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

    public static EstadoCivil fromChromosome(Chromosome<BitGene> chromosome) {
        int gene0 = chromosome.getGene(6).getBit() ? 1 : 0;
        int gene1 = chromosome.getGene(7).getBit() ? 1 : 0;
        int genes = gene0 | (gene1 << 1);
        return Arrays
                .stream(values())
                .filter(apellido -> apellido.value == genes)
                .findFirst()
                .get();
    }
}
