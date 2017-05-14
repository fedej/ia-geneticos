package ar.com.inteligencia;

import ar.com.inteligencia.atributo.Nombre;
import ar.com.inteligencia.regla.MotorDeReglas;
import org.jenetics.BitChromosome;
import org.jenetics.BitGene;
import org.jenetics.Chromosome;
import org.jenetics.ExponentialRankSelector;
import org.jenetics.Genotype;
import org.jenetics.MultiPointCrossover;
import org.jenetics.Mutator;
import org.jenetics.Optimize;
import org.jenetics.Phenotype;
import org.jenetics.RouletteWheelSelector;
import org.jenetics.SinglePointCrossover;
import org.jenetics.TournamentSelector;
import org.jenetics.engine.Engine;
import org.jenetics.engine.EvolutionResult;
import org.jenetics.engine.EvolutionStatistics;
import org.jenetics.engine.limit;
import org.jenetics.util.Factory;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.jenetics.engine.limit.bySteadyFitness;

public class Application {


    // 2.) Definition of the fitness function.
    private static Double eval(Genotype<BitGene> gt) {
        //Sospechoso felix = getSospechosos(gt.getChromosome(0));
        //Sospechoso gustavo = getSospechosos(gt.getChromosome(1));
        //Sospechoso jesus = getSospechosos(gt.getChromosome(2));
        //Sospechoso ramiro = getSospechosos(gt.getChromosome(3));
        //return new MotorDeReglas(felix, gustavo, jesus, ramiro).evaluar();
        return new MotorDeReglas(gt).evaluar();
    }

    public static void main(String[] args) {
        // 1.) Define the genotype (factory) suitable
        //     for the problem.

        final Factory<Genotype<BitGene>> gtf =
                Genotype.of(BitChromosome.of(60));

        // 3.) Create the execution environment.
        final Engine<BitGene, Double> engine = Engine
                .builder(Application::eval, gtf)
                .populationSize((int) Math.pow(2, 18))
                .maximalPhenotypeAge(5L)
                .optimize(Optimize.MAXIMUM)
                .alterers(new Mutator<>(0.1), new MultiPointCrossover<>(0.03D, 5))
                //.survivorsSelector(new ExponentialRankSelector<>())
                .offspringSelector(new RouletteWheelSelector<>())
                .genotypeValidator(g -> new MotorDeReglas(g).isValid())
                .build();

        // 4.) Start the execution (evolution) and
        //     collect the result.

        // Create evolution statistics consumer.
        final EvolutionStatistics<Double, ?>
                statistics = EvolutionStatistics.ofNumber();

        List<Double> a = new ArrayList<>();

        final Phenotype<BitGene, Double> res = engine.stream()
                .limit(limit.byFitnessThreshold(138D))
                .limit(50)
                .peek(statistics)
                .peek(e -> a.add(e.getBestFitness()))
                .collect(EvolutionResult.toBestPhenotype());

        System.out.println(statistics);
        System.out.println("Valores" + a);

        final Chromosome<BitGene> chromosome = res.getGenotype().getChromosome();

        boolean[] felix = new boolean[15];
        for (int i = 0; i < 15; i++) {
            felix[i] = chromosome.getGene(i).getBit();
        }

        boolean[] gustavo = new boolean[15];
        for (int i = 15; i < 30; i++) {
            gustavo[i - 15] = chromosome.getGene(i).getBit();
        }

        boolean[] jesus = new boolean[15];
        for (int i = 30; i < 45; i++) {
            jesus[i - 30] = chromosome.getGene(i).getBit();
        }

        boolean[] ramiro = new boolean[15];
        for (int i = 45; i < 60; i++) {
            ramiro[i - 45] = chromosome.getGene(i).getBit();
        }

        Stream.of(new Sospechoso(Nombre.FELIX, felix),
                new Sospechoso(Nombre.GUSTAVO, gustavo),
                new Sospechoso(Nombre.JESUS, jesus),
                new Sospechoso(Nombre.RAMIRO, ramiro))
                .forEach(System.out::println);

    }


}
