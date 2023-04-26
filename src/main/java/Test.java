import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static java.lang.System.out;

public class Test {
    @Option(name="-i", required = true, usage = "Input file path")
    private static String file;
    public static HashMap<String, Node> nodeMap = new HashMap<>();
    List<String> lines;

    final CmdLineParser parser = new CmdLineParser(this);

    public static void main(String[] args) throws InterruptedException {
        final Test test = new Test();

        test.getArguments(args);
        test.getLines(file);
        test.getNodes();

        for(Node node : nodeMap.values()) {
            node.start();
        }
    }
    public void getArguments(final String[] args){
        if (args.length != 2) {
            parser.printUsage(out);
            System.exit(-1);
        }

        try{
            parser.parseArgument(args);
            if (!Files.exists(Paths.get(file))) {
                out.println("File not exists for given path!");
                System.exit(-1);
            }
        } catch (CmdLineException exception) {
            out.println("Error to parse arguments: " + exception);
        }
    }

    void getLines(String path) {
        try {
            this.lines = Files.readAllLines(Paths.get(file));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void getNodes() {
        for (String s: this.lines) {
            Node newNode;
            Node waitNode;

            if (!s.contains("->")){
                if (nodeMap.get(s) == null) {
                    newNode = new Node(s);
                    nodeMap.put(newNode.getNodeName(), newNode);
                }
            } else {
                String[] split = s.split("->");
                String[] nodeQueue = split[0].split(",");

                if (nodeMap.get(split[1]) == null) {
                    newNode = new Node(split[1]);
                    nodeMap.put(newNode.getNodeName(), newNode);
                }

                for (String node : nodeQueue) {

                    if(nodeMap.get(node) == null) {
                        waitNode = new Node(node);
                        nodeMap.put(node, waitNode);
                        nodeMap.get(split[1]).add(waitNode);
                    } else {
                        nodeMap.get(split[1]).add(nodeMap.get(node));
                    }
                }
            }
        }
    }
}
