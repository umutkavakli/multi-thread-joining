import java.util.*;

public class Node extends Thread {
    private String name;
    private Set<Node> waitList;

    public Node(String name) {
        this.name = name;
        waitList = new TreeSet<>(Comparator.comparing(o -> o.name)) ;
    }

    void perform() {
        try {
            System.out.printf("Node%s is being started\n", this.name);
            Thread.sleep(new Random().nextInt(2000));
            System.out.printf("Node%s is completed\n", this.name);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        if (!waitList.isEmpty()) {
            String queue = "";
            int i = 0;

            for (Node node : waitList) {
                i++;
                queue += node.name + ((i == waitList.size()) ? "" : ",");
            }
            System.out.printf("Node%s is waiting for %s\n", this.name, queue);
        }

        for (Node priorNode : waitList) {
            try {
                priorNode.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        perform();
    }

    public String getNodeName() {
        return this.name;
    }
    void add(Node prior) {
        waitList.add(prior);
    }


}
