package org.rhw.bmr.project.common.algo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

/**
 * 更完善、更高效的 PageRank 算法，不依赖任何第三方图算法库。
 * 包含反向邻接表、对悬空节点的处理，以及收敛判断。
 */
public class CustomPageRankAlgorithm {

    /**
     * 计算 PageRank 主方法
     *
     * @param graph         正向邻接表：key 为书籍 ID，value 为与其直接相连的所有书籍 ID
     * @param dampingFactor 阻尼系数，一般取 0.85
     * @param maxIterations 最大迭代次数
     * @param epsilon       收敛阈值，如果两次迭代之间的差值小于该值就提前结束
     * @return 返回每个书籍 ID 对应的 PageRank 值
     */
    public static Map<Long, Double> computePageRank(Map<Long, List<Long>> graph,
                                                    double dampingFactor,
                                                    int maxIterations,
                                                    double epsilon) {
        // 0. 特殊情况：如果图为空，直接返回空
        if (graph == null || graph.isEmpty()) {
            return new HashMap<Long, Double>();
        }

        // 1. 构建反向邻接表(Reverse Graph) 和 出度表(OutDegree)
        Map<Long, List<Long>> reverseGraph = buildReverseGraph(graph);
        Map<Long, Integer> outDegreeMap = buildOutDegreeMap(graph);

        int nodeCount = graph.size();
        // 初始化 PageRank 值：通常可以设为 1.0 / nodeCount
        Map<Long, Double> pageRank = new HashMap<Long, Double>();
        for (Long node : graph.keySet()) {
            pageRank.put(node, 1.0 / nodeCount);
        }

        // 2. 迭代计算 PageRank
        for (int iter = 0; iter < maxIterations; iter++) {
            // 2.1 计算所有“悬空节点”的 rank 总和
            //    如果某个节点 outDegree = 0，则它的全部 PR 会在下一次迭代中随机分配给所有节点
            double danglingSum = 0.0;
            for (Long node : graph.keySet()) {
                int outDeg = outDegreeMap.get(node);
                if (outDeg == 0) {
                    danglingSum += pageRank.get(node);
                }
            }

            // 2.2 逐节点计算新的 PageRank
            Map<Long, Double> newPageRank = new HashMap<Long, Double>();
            for (Long node : graph.keySet()) {
                // 基础分 (来自随机跳转) + “悬空节点贡献” 的平均分配
                double rank = (1.0 - dampingFactor) / nodeCount
                        + dampingFactor * (danglingSum / nodeCount);

                // 来自反向邻接表中指向该节点的所有节点的贡献
                List<Long> inNeighbors = reverseGraph.get(node);
                if (inNeighbors != null && !inNeighbors.isEmpty()) {
                    for (Long inNode : inNeighbors) {
                        int outDeg = outDegreeMap.get(inNode);
                        if (outDeg > 0) {
                            rank += dampingFactor * (pageRank.get(inNode) / outDeg);
                        }
                    }
                }

                newPageRank.put(node, rank);
            }

            // 2.3 判断本轮与上一轮的差值，若小于阈值 epsilon，则提前结束
            double diff = calculateDifference(pageRank, newPageRank);
            pageRank = newPageRank; // 更新

            if (diff < epsilon) {
                // System.out.println("PageRank 迭代在第 " + iter + " 次收敛，diff = " + diff);
                break;
            }
        }

        return pageRank;
    }


    /**
     * 根据正向邻接表构建反向邻接表
     * reverseGraph[node] 中存储所有指向 node 的节点
     */
    private static Map<Long, List<Long>> buildReverseGraph(Map<Long, List<Long>> graph) {
        Map<Long, List<Long>> reverseGraph = new HashMap<Long, List<Long>>();
        // 初始化 key
        for (Long node : graph.keySet()) {
            reverseGraph.put(node, new ArrayList<Long>());
        }
        // 构建反向边
        for (Long node : graph.keySet()) {
            List<Long> outNodes = graph.get(node);
            if (outNodes != null) {
                for (Long outNode : outNodes) {
                    // outNode 的入边里加上 node
                    reverseGraph.get(outNode).add(node);
                }
            }
        }
        return reverseGraph;
    }

    /**
     * 构建每个节点的出度表
     */
    private static Map<Long, Integer> buildOutDegreeMap(Map<Long, List<Long>> graph) {
        Map<Long, Integer> outDegreeMap = new HashMap<Long, Integer>();
        for (Long node : graph.keySet()) {
            List<Long> outList = graph.get(node);
            int outDegree = (outList == null) ? 0 : outList.size();
            outDegreeMap.put(node, outDegree);
        }
        return outDegreeMap;
    }

    /**
     * 计算两次迭代之间的 PageRank 值差异之和
     */
    private static double calculateDifference(Map<Long, Double> oldPR, Map<Long, Double> newPR) {
        double diff = 0.0;
        for (Long node : oldPR.keySet()) {
            diff += Math.abs(oldPR.get(node) - newPR.get(node));
        }
        return diff;
    }
}
