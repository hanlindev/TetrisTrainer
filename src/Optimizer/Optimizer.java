package Optimizer;
// Optimize feature weights using PSO
import net.sourceforge.jswarm_pso.*;
import java.io.*;
import java.util.concurrent.*;
import java.util.*;

import parallelpso.*;
public class Optimizer {
	public static int iter;
	static public double[] parameters = {-3.3200740, 2.70317569, -2.7157289, -5.1061407, -6.9380080, -2.4075407, -1.0};
	static public void main(String[] args) throws Exception{
		int numProcess = Integer.parseInt(args[0]);
		int iterations = Integer.parseInt(args[1]);
		String path = args[2];
		BufferedWriter bw = new BufferedWriter(new FileWriter(path));
		MyParticle aParticle = new MyParticle();
		ParallelSwarm swarm = new ParallelSwarm(36, aParticle, new MyFitnessFunction(numProcess), numProcess);
		double inertia = 0.72, particleInc = 1.42, globalInc = 1.42, maxVelocity = 0.5;
		
		swarm.setInertia(inertia);
		swarm.setParticleIncrement(particleInc);
		swarm.setGlobalIncrement(globalInc);
		swarm.setMaxMinVelocity(maxVelocity);
		swarm.setMaxPosition(10);
		swarm.setMinPosition(-10);
		
		for (int i = 0; i < iterations; ++i) {
			iter = i;
			swarm.evolve();
			double[] bestPosition = swarm.getBestPosition();
			String comma = "", out = "";
			for (int j = 0; j < bestPosition.length; ++j) {
				out += comma + bestPosition[j];
				comma = ", ";
			}
			out += "\n";
			bw.write(out);
			bw.flush();
		}
		// Shutdown protocal
		swarm.mainPool.shutdown();
		for (MyFitnessFunction fi : swarm.particleFitnessFunction) {
			fi.mainPool.shutdown();
		}
		bw.write(swarm.toStringStats() + "\n");
		bw.flush();
		bw.close();
		System.out.println(swarm.toStringStats());
	}
}
