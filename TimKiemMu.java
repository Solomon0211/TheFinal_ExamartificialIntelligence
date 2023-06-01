import java.io.*;
import java.util.*;

class Edge {
    private int x;
    private int y;

    private int weight;

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Edge(int x, int y, int weight) {
        this.x = x;
        this.y = y;
        this.weight = weight;
    }

    public static void generateWeight(ArrayList<Edge> edges) {
        HashMap<String, Integer> weightMap = new HashMap<>();
        for (Edge edge : edges) {
            String key = Math.min(edge.getX(), edge.getY()) + "," + Math.max(edge.getX(), edge.getY());
            if (weightMap.containsKey(key)) {
                edge.setWeight(weightMap.get(key));
            } else {
                int weight = (int) (Math.random() * 5) + 5;
                weightMap.put(key, weight);
                edge.setWeight(weight);
            }
        }
    }
}

class Data{
    private ArrayList<Edge> graph = new ArrayList<>();
    private int numVertex;

    public Data(){
        String filename = "src/abc.txt"; // tên file cần đọc
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename)); // Tạo đối tượng BufferedReader để đọc file

            String line;
            while ((line = reader.readLine()) != null) { // Đọc từng dòng tiếp theo
                String[] tokens = line.split(" ");
                int x = Integer.parseInt(tokens[0]); // Đọc giá trị x
                int y = Integer.parseInt(tokens[1]); // Đọc giá trị y
                int z = Integer.parseInt(tokens[2]);
                this.graph.add(new Edge(x, y,z)); // Thêm một cạnh mới vào Graph
            }
            this.numVertex = this.graph.size();
//            Edge.generateWeight(graph);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Edge> getGraph() {
        return graph;
    }

    public void setGraph(ArrayList<Edge> graph) {
        this.graph = graph;
    }

    public int getNumVertex() {
        return numVertex;
    }

    public void setNumVertex(int numVertex) {
        this.numVertex = numVertex;
    }
}

class Node {
    private int vertex;
    private int cost;

    public Node(int vertex, int cost) {
        this.vertex = vertex;
        this.cost = cost;
    }

    public int getVertex() {
        return vertex;
    }

    public int getCost() {
        return cost;
    }
}

class Algorithm{
    private Data data;

    public ArrayList<Integer> bfs(int start, int end) {
        data = new Data();
        ArrayList<Edge> graph = data.getGraph();
        int numVertex = data.getNumVertex();

        boolean[] visited = new boolean[numVertex];

        // Khởi tạo một hàng đợi để lưu các đỉnh cần duyệt
        Queue<Integer> queue = new LinkedList<>();
        int[] parent = new int[numVertex]; // mảng truy vết

        queue.add(start);

        visited[start] = true;
        parent[start] = -1;
        ArrayList<Integer> path = new ArrayList<>();

        while (!queue.isEmpty()) {
            int vertex = queue.poll();

            if (vertex == end) {
                break;
            }
            // Duyệt qua các đỉnh kề của đỉnh hiện tại
            for (Edge edge : graph) {
                if (edge.getX() == vertex && !visited[edge.getY()]) {
                    // Nếu đỉnh kề chưa được duyệt, thêm nó vào hàng đợi
                    queue.add(edge.getY());
                    parent[edge.getY()] = vertex;
                    // Đánh dấu đỉnh kề đã được duyệt
                    visited[edge.getY()] = true;
                }
            }
        }
        // truy vết từ đỉnh end để lấy đường đi
        if (visited[end]) {
            int v = end;
            while (v != -1) {
                path.add(v);
                v = parent[v];
            }
            Collections.reverse(path);
        }
        return path;
    }

    public ArrayList<Integer> dfs(int start, int end) {
        data = new Data();
        ArrayList<Edge> graph = data.getGraph();
        int numVertex = data.getNumVertex();

        ArrayList<Integer>[] adjacencyList = new ArrayList[numVertex]; // danh sách kề
        boolean[] visited = new boolean[numVertex]; // đánh dấu các đỉnh đã được thăm qua
        int[] parent = new int[numVertex]; // mảng truy vết
        Stack<Integer> stack = new Stack<>(); // ngăn xếp để duyệt DFS
        ArrayList<Integer> path = new ArrayList<>(); // đường đi giữa hai đỉnh

        // khởi tạo danh sách kề
        for (int i = 0; i < numVertex; i++) {
            adjacencyList[i] = new ArrayList<>();
        }
        for (Edge e : graph) {
            adjacencyList[e.getX()].add(e.getY());
        }
        // bắt đầu duyệt DFS từ đỉnh start
        stack.push(start);
        visited[start] = true;
        parent[start] = -1;
        while (!stack.isEmpty()) {
            int u = stack.pop();
            for (int v : adjacencyList[u]) {
                if (!visited[v]) {
                    visited[v] = true;
                    parent[v] = u;
                    stack.push(v);
                }
            }
        }
        // truy vết từ đỉnh end để lấy đường đi
        if (visited[end]) {
            int v = end;
            while (v != -1) {
                path.add(v);
                v = parent[v];
            }
            Collections.reverse(path);
        }
        return path;
    }

    public ArrayList<Integer> dls(int start, int end, int depthLimit) {
        data = new Data();
        ArrayList<Edge> graph = data.getGraph();
        int numVertex = data.getNumVertex();

        int []depth = new int[numVertex];

        ArrayList<Integer>[] adjacencyList = new ArrayList[numVertex]; // danh sách kề
        boolean[] visited = new boolean[numVertex]; // đánh dấu các đỉnh đã được thăm qua
        int[] parent = new int[numVertex]; // mảng truy vết
        Stack<Integer> stack = new Stack<>(); // ngăn xếp để duyệt DFS
        ArrayList<Integer> path = new ArrayList<>(); // đường đi giữa hai đỉnh

        // khởi tạo danh sách kề
        for (int i = 0; i < numVertex; i++) {
            adjacencyList[i] = new ArrayList<>();
        }
        for (Edge e : graph) {
            adjacencyList[e.getX()].add(e.getY());
        }

        stack.push(start);
        visited[start] = true;

        parent[start] = -1;

        while (!stack.isEmpty()) {
            int u = stack.pop();
            for (int v : adjacencyList[u]) {
                depth[v] = depth[u] + 1;
                if (!visited[v] && depth[v] <= depthLimit) {
                    visited[v] = true;
                    parent[v] = u;
                    stack.push(v);
                }
            }
        }
        // truy vết từ đỉnh end để lấy đường đi
        if (visited[end]) {
            int v = end;
            while (v != -1) {
                path.add(v);
                v = parent[v];
            }
            Collections.reverse(path);
        }
        return path;
    }

    public ArrayList<Integer> ids(int start, int end) {
        data = new Data();
        ArrayList<Edge> graph = data.getGraph();
        int numVertex = data.getNumVertex();

        int maxDepth = numVertex; // Độ sâu tối đa
        ArrayList<Integer> path = new ArrayList<>();
        for (int depth = 0; depth <= maxDepth; depth++) { // Thực hiện tìm kiếm theo độ sâu từ 0 đến maxDepth
            path = dls(start, end, depth); // Gọi hàm DLS để tìm đường đi
            if (!path.isEmpty()) { // Nếu path không rỗng chứng tỏ đã tìm được đường đi
                return path;
            }
        }
        return path; // Return 1 ArrayList rỗng
    }

    public ArrayList<Integer> ucs(int start, int end, Data dt) {
        ArrayList<Edge> graph = dt.getGraph();
        int numVertex = dt.getNumVertex();

        // khởi tạo mảng visited và parent
        boolean[] visited = new boolean[numVertex];
        int[] parent = new int[numVertex];


        // khởi tạo priorityQueue
        PriorityQueue<Node> queue = new PriorityQueue<>(new Comparator<Node>() {
            @Override
            public int compare(Node n1, Node n2) {
                return n1.getCost() - n2.getCost();
            }
        });
        queue.add(new Node(start, 0));
        parent[start] = -1;

        while (!queue.isEmpty()) {
            // lấy đỉnh có chi phí nhỏ nhất
            Node node = queue.poll();
            int vertex = node.getVertex();

            // kiểm tra xem đỉnh đã được duyệt chưa
            if (visited[vertex]) {
                continue;
            }
            visited[vertex] = true;

            // nếu đỉnh là đích thì truy vết để lấy đường đi
            int sum = 0;
            if (vertex == end) {
                ArrayList<Integer> path = new ArrayList<>();
                int v = end;
                while (v != -1) {
                    path.add(v);
                    if (parent[v] != -1) {
                        for (Edge edge : graph) {
                            if (edge.getX() == parent[v] && edge.getY() == v) {
                                sum += edge.getWeight();
                                break;
                            }
                        }
                    }
                    v = parent[v];
                }
                Collections.reverse(path);
                path.add(sum);
                return path;
            }

            // duyệt qua các đỉnh kề của đỉnh hiện tại
            for (Edge edge : graph) {
                if (edge.getX() == vertex && !visited[edge.getY()]) {
                    // thêm đỉnh kề vào hàng đợi với chi phí là tổng chi phí từ đỉnh bắt đầu đến đỉnh hiện tại cộng với chi phí từ đỉnh hiện tại đến đỉnh kề
                    queue.add(new Node(edge.getY(), node.getCost() + edge.getWeight()));
                    // cập nhật đỉnh cha của đỉnh kề
                    parent[edge.getY()] = vertex;
                }
            }
        }
        return null;
    }
}

class FileUtil {
    public static void writeArrayListToFile(String filePath, ArrayList<Integer> arrayList) {
        try {
            PrintWriter pw = new PrintWriter(new FileOutputStream(new File(filePath)));
            for (int i = 0; i < arrayList.size(); i++) {
                pw.println(arrayList.get(i));
            }
            pw.close();
            System.out.println("Đã ghi dữ liệu vào file thành công!");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}

public class TimKiemMu {
    public static void main(String[] args) {
        Algorithm al = new Algorithm();
        Data u = new Data();
        for(Edge i: u.getGraph()){
            System.out.println(i.getX() + " " + i.getY() + " " + i.getWeight());
        }
        System.out.println("BFS: ");
        for (int i : al.bfs(0,4)) System.out.print(i + " ");
        System.out.println("\nDFS: ");
        for (int i : al.dfs(0,4)) System.out.print(i + " ");
        System.out.println("\nDLS: ");
        for (int i : al.dls(0,4, 6)) System.out.print(i + " ");
        System.out.println("\nIDS: ");
        for (int i : al.ids(0,4)) System.out.print(i + " ");
        System.out.println("\nUCS: ");
        for (int i : al.ucs(0,4, u)) System.out.print(i + " ");

        System.out.println();
        FileUtil.writeArrayListToFile("src/bfs.txt", al.bfs(0,4));
        FileUtil.writeArrayListToFile("src/dfs.txt", al.dfs(0,4));
        FileUtil.writeArrayListToFile("src/dls.txt", al.dls(0,4, 6));
        FileUtil.writeArrayListToFile("src/ids.txt", al.ids(0,4));
        FileUtil.writeArrayListToFile("src/ucs.txt", al.ucs(0,4, u));
    }
}
