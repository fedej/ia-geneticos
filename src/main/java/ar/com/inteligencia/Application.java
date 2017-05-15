package ar.com.inteligencia;

import ar.com.inteligencia.atributo.Nombre;
import ar.com.inteligencia.regla.MotorDeReglas;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import org.jenetics.BitChromosome;
import org.jenetics.BitGene;
import org.jenetics.Chromosome;
import org.jenetics.Genotype;
import org.jenetics.MultiPointCrossover;
import org.jenetics.Mutator;
import org.jenetics.Optimize;
import org.jenetics.Phenotype;
import org.jenetics.RouletteWheelSelector;
import org.jenetics.engine.Engine;
import org.jenetics.engine.EvolutionResult;
import org.jenetics.engine.EvolutionStatistics;
import org.jenetics.engine.limit;
import org.jenetics.util.Factory;

import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

public class Application extends javafx.application.Application {

    private static final XYChart.Series<Number, Number> SERIES = new XYChart.Series<>();

    // 2.) Definition of the fitness function.
    private static Double eval(Genotype<BitGene> gt) {
        return new MotorDeReglas(gt).evaluar();
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
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
                .offspringSelector(new RouletteWheelSelector<>())
                .genotypeValidator(g -> new MotorDeReglas(g).isValid())
                .build();

        Platform.runLater(
                () -> {

                    // 4.) Start the execution (evolution) and
                    //     collect the result.
                    // Create evolution statistics consumer.
                    final EvolutionStatistics<Double, ?>
                            statistics = EvolutionStatistics.ofNumber();

                    final Phenotype<BitGene, Double> result =
                            engine.stream()
                                    .limit(limit.byFitnessThreshold(138D))
                                    .limit(50)
                                    .peek(e ->
                                            SERIES
                                                    .getData()
                                                    .add(new XYChart.Data<Number, Number>(e.getGeneration(), e.getBestFitness())))
                                    .peek(statistics)
                                    .collect(EvolutionResult.toBestPhenotype());

                    System.out.println(statistics);
                    final Chromosome<BitGene> chromosome = result.getGenotype().getChromosome();

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
        );

        launch(args);


    }


    @Override
    public void start(final Stage stage) throws Exception {
        stage.setTitle("Evaluacion");
        final NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Iteraction");
        final LineChart<Number, Number> lineChart = new LineChart<>(xAxis, new NumberAxis());
        lineChart.setHorizontalGridLinesVisible(true);
        lineChart.setTitle("Funcion de aptitud");
        SERIES.setName("Valor");
        final Scene scene = new Scene(lineChart, 800, 600);
        lineChart.getData().add(SERIES);
        stage.setScene(scene);
        stage.show();
    }
}
