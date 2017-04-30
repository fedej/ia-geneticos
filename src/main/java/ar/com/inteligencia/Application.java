package ar.com.inteligencia;

import ar.com.inteligencia.regla.MotorDeReglas;
import org.jenetics.BitChromosome;
import org.jenetics.BitGene;
import org.jenetics.Chromosome;
import org.jenetics.ExponentialRankSelector;
import org.jenetics.Genotype;
import org.jenetics.Mutator;
import org.jenetics.Optimize;
import org.jenetics.Phenotype;
import org.jenetics.SinglePointCrossover;
import org.jenetics.TournamentSelector;
import org.jenetics.engine.Engine;
import org.jenetics.engine.EvolutionResult;
import org.jenetics.engine.EvolutionStatistics;
import org.jenetics.util.Factory;

import static org.jenetics.engine.limit.bySteadyFitness;

public class Application {

    private static Sospechoso getSospechosos(Chromosome<BitGene> chromosome) {
        return new Sospechoso(chromosome);
    }

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
                Genotype.of(BitChromosome.of(17), 4);

        // 3.) Create the execution environment.
        final Engine<BitGene, Double> engine = Engine
                .builder(Application::eval, gtf)
                .populationSize((int) Math.pow(2, 10))
                .maximalPhenotypeAge(5L)
                .optimize(Optimize.MAXIMUM)
                .alterers(new Mutator<>(0.55), new SinglePointCrossover<>(0.06))
                .survivorsSelector(new ExponentialRankSelector<>())
                .offspringSelector(new TournamentSelector<>())
                .genotypeValidator(g -> {
                    /*Sospechoso felix = getSospechosos(g.getChromosome(0));
                    Sospechoso gustavo = getSospechosos(g.getChromosome(1));
                    Sospechoso jesus = getSospechosos(g.getChromosome(2));
                    Sospechoso ramiro = getSospechosos(g.getChromosome(3));*/
                    return new MotorDeReglas(g).isValid();
                })
                .build();

        // 4.) Start the execution (evolution) and
        //     collect the result.

        // Create evolution statistics consumer.
        final EvolutionStatistics<Double, ?>
                statistics = EvolutionStatistics.ofNumber();

        final Phenotype<BitGene, Double> res = engine.stream()
                .limit(bySteadyFitness(10))
                .limit(100)
                .peek(statistics)
                .collect(EvolutionResult.toBestPhenotype());

        System.out.println(statistics);
        res.getGenotype().stream().map(Sospechoso::new).forEach(System.out::println);

    }


}
