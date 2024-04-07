import java.util.HashMap;
import java.util.Map;
import java.util.Random;

class Shooter implements Runnable {
    private static Map<Long, Integer> disksHitMap = new HashMap<>(); // Mapa para mantener los recuentos locales
    private int successProbability; // Probabilidad de golpear un disco (de 10)
    private int numDisks;

    public Shooter(int successProbability, int numDisks) {
        this.successProbability = successProbability;
        this.numDisks = numDisks;
    }

    public int getSucces(){
        return successProbability;
    }

    @Override
    public void run() {
        Random random = new Random();
        int localCount = 0;
        for (int i = 0; i < numDisks; i++) {
            if (random.nextInt(10) < successProbability) { // Golpear un disco
                localCount++;
            }
        }
        // Agregar el contador local al Mapa con el ID del hilo como clave
        disksHitMap.put(Thread.currentThread().getId(), localCount);
    }

    public static int getTotalDisksHit() {
        int total = 0;
        for (int count : disksHitMap.values()) {
            total += count;
        }
        return total;
    }

    public static void main(String[] args) throws InterruptedException {
        int numDisks = 1000; // Puedes ajustar este número según tus requisitos
        int numShooters = 2; // Número de tiradores

        Thread[] threads = new Thread[numShooters];

        // Iniciar todos los hilos
        for (int i = 0; i < numShooters; i++) {
            Shooter shooter = new Shooter( i == 0 ? 9 : 8, numDisks);
            threads[i] = new Thread(shooter);
            threads[i].start();
        }

        // Unirse a todos los hilos
        for (int i = 0; i < numShooters; i++) {
            threads[i].join();
        }

        // Calcular el total de discos golpeados
        /**
         * Shooter 1 -> Ocelot
         * Shppter 2 -> Venom Snake
         */
        int totalDisksHit = Shooter.getTotalDisksHit();
        System.out.println(" <<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println(" Total de discos por shooter : " + numDisks);
        System.out.println(" Total de discos golpeados: " + totalDisksHit + "/" + numDisks *  numShooters);
        int i = 0;
        for (Map.Entry<Long, Integer> entry : disksHitMap.entrySet()) {
            i ++;
            System.out.println(" Shooter " + i + " , Disk Hit: " + entry.getValue());
        }
        System.out.println(" <<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>");
    }
}