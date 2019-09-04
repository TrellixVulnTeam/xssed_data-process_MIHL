package tech.ganecheng.id3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

public class Test {

	// �����б�
	public static List<String> featureList = new ArrayList<String>();
	// ����ֵ�б�
	public static List<List<String>> featureValueTableList = new ArrayList<List<String>>();
	// �õ�ȫ������
	public static Map<Integer, List<String>> tableMap = new HashMap<Integer, List<String>>();

	public static void main(String[] args)// ������
	{
		// ��ʼ������
		readOriginalData(new File("src/data.txt"));
		for (String f : featureList) {
			// System.out.println(f);
			System.out.print(f + ",");
		}
		System.out.println("\n");// ��������������ݼ��һ��
		// ������ݼ����б�
		List<Integer> tempDataList = new ArrayList<Integer>();
		for (Map.Entry<Integer, List<String>> entry : tableMap.entrySet()) {
			System.out.print(entry.getKey() + ",");

			for (String s : entry.getValue()) {
				System.out.print(s + ",");
			}
			System.out.println();

			tempDataList.add(entry.getKey());
		}

		// �õ��������б�
		List<Integer> featureIndexList = new ArrayList<Integer>();
		for (int i = 0; i < featureList.size(); i++) {
			featureIndexList.add(i);
		}

		// ���������
		Node decisionTree = createDecisionTree(tempDataList, featureIndexList,
				null);

		// ������ļ���
		String outputFilePath = "E:/id3.xml";
		writeToXML(decisionTree, outputFilePath);

		System.out.println("�жϽ��:"
				+ getDTAnswer(decisionTree, featureList,
						Arrays.asList("rainy,cool,high,TRUE".split(","))));
	}

	/**
	 * ����������ݵõ���������Ԥ����
	 * 
	 * @param decisionTree
	 *            ������
	 * @param featureList
	 *            �����б�
	 * @param testDataList
	 *            ��������
	 * @return
	 */
	public static String getDTAnswer(Node decisionTree,
			List<String> featureList, List<String> testDataList) {
		if (featureList.size() - 1 != testDataList.size()) {
			System.out.println("�������ݲ�����");
			return "ERROR";
		}

		while (decisionTree != null) {
			// ������ӽڵ�Ϊ��,�򷵻ش˽ڵ��.
			if (decisionTree.childrenNodeList == null
					|| decisionTree.childrenNodeList.size() <= 0) {
				return decisionTree.featureName;
			}
			// ���ӽڵ㲻Ϊ��,���ж�����ֵ�ҵ��ӽڵ�
			for (int i = 0; i < featureList.size() - 1; i++) {
				// �ҵ���ǰ�����±�
				if (featureList.get(i).equals(decisionTree.featureName)) {
					// �õ�������������ֵ
					String featureValue = testDataList.get(i);
					// ���ӽڵ����ҵ����д�����ֵ�Ľڵ�
					Node childNode = null;
					for (Node cn : decisionTree.childrenNodeList) {
						if (cn.lastFeatureValue.equals(featureValue)) {
							childNode = cn;
							break;
						}
					}
					// ���û���ҵ��˽ڵ�,��˵��ѵ������û�е�����ڵ������ֵ
					if (childNode == null) {
						System.out.println("û���ҵ�������ֵ������");
						return "ERROR";
					}

					decisionTree = childNode;
					break;
				}
			}
		}
		return "ERROR";
	}

	/**
	 * ����������
	 * 
	 * @param dataSetList
	 *            ���ݼ�
	 * @param featureIndexList
	 *            ���õ������б�
	 * @param lastFeatureValue
	 *            ����˽ڵ����һ������ֵ
	 * @return
	 */
	public static Node createDecisionTree(List<Integer> dataSetList,
			List<Integer> featureIndexList, String lastFeatureValue) {
		// ���ֻ��һ��ֵ�Ļ�,��ֱ�ӷ���Ҷ�ӽڵ�
		int valueIndex = featureIndexList.get(featureIndexList.size() - 1);
		// ѡ���һ��ֵ
		String firstValue = tableMap.get(dataSetList.get(0)).get(valueIndex);
		int firstValueNum = 0;
		for (Integer id : dataSetList) {
			if (firstValue.equals(tableMap.get(id).get(valueIndex))) {
				firstValueNum++;
			}
		}
		if (firstValueNum == dataSetList.size()) {
			Node node = new Node();
			node.lastFeatureValue = lastFeatureValue;
			node.featureName = firstValue;
			node.childrenNodeList = null;
			return node;
		}

		// ��������������ʱ����ֵ��û����ȫ��ͬ,���ض�������Ľ��
		if (featureIndexList.size() == 1) {
			Node node = new Node();
			node.lastFeatureValue = lastFeatureValue;
			node.featureName = majorityVote(dataSetList);
			node.childrenNodeList = null;
			return node;
		}

		// �����Ϣ������������
		int bestFeatureIndex = chooseBestFeatureToSplit(dataSetList,
				featureIndexList);
		// �õ���������ȫ�ֵ��±�
		int realFeatureIndex = featureIndexList.get(bestFeatureIndex);
		String bestFeatureName = featureList.get(realFeatureIndex);

		// ���������
		Node node = new Node();
		node.lastFeatureValue = lastFeatureValue;
		node.featureName = bestFeatureName;

		// �õ���������ֵ�ļ���
		List<String> featureValueList = featureValueTableList
				.get(realFeatureIndex);

		// ɾ��������
		featureIndexList.remove(bestFeatureIndex);

		// ������������ֵ,�������ݼ�,Ȼ��ݹ�õ��ӽڵ�
		for (String fv : featureValueList) {
			// �õ������ݼ�
			List<Integer> subDataSetList = splitDataSet(dataSetList,
					realFeatureIndex, fv);
			// ��������ݼ�Ϊ�գ���ʹ�ö��������һ���𰸡�
			if (subDataSetList == null || subDataSetList.size() <= 0) {
				Node childNode = new Node();
				childNode.lastFeatureValue = fv;
				childNode.featureName = majorityVote(dataSetList);
				childNode.childrenNodeList = null;
				node.childrenNodeList.add(childNode);
				break;
			}
			// ����ӽڵ�
			Node childNode = createDecisionTree(subDataSetList,
					featureIndexList, fv);
			node.childrenNodeList.add(childNode);
		}

		return node;
	}

	/**
	 * ��������õ����ִ��������Ǹ�ֵ
	 * 
	 * @param dataSetList
	 * @return
	 */
	public static String majorityVote(List<Integer> dataSetList) {
		// �õ����
		int resultIndex = tableMap.get(dataSetList.get(0)).size() - 1;
		Map<String, Integer> valueMap = new HashMap<String, Integer>();
		for (Integer id : dataSetList) {
			String value = tableMap.get(id).get(resultIndex);
			Integer num = valueMap.get(value);
			if (num == null || num == 0) {
				num = 0;
			}
			valueMap.put(value, num + 1);
		}

		int maxNum = 0;
		String value = "";

		for (Map.Entry<String, Integer> entry : valueMap.entrySet()) {
			if (entry.getValue() > maxNum) {
				maxNum = entry.getValue();
				value = entry.getKey();
			}
		}

		return value;
	}

	/**
	 * ��ָ���ļ���������ѡ��һ���������(��Ϣ�������)���ڻ������ݼ�
	 * 
	 * @param dataSetList
	 * @return ��������������±�
	 */
	public static int chooseBestFeatureToSplit(List<Integer> dataSetList,
			List<Integer> featureIndexList) {
		double baseEntropy = calculateEntropy(dataSetList);
		double bestInformationGain = 0;
		int bestFeature = -1;

		// ѭ��������������
		for (int temp = 0; temp < featureIndexList.size() - 1; temp++) {
			int i = featureIndexList.get(temp);

			// �õ���������
			List<String> featureValueList = new ArrayList<String>();
			for (Integer id : dataSetList) {
				String value = tableMap.get(id).get(i);
				featureValueList.add(value);
			}
			Set<String> featureValueSet = new HashSet<String>();
			featureValueSet.addAll(featureValueList);

			// �õ��˷����µ���
			double newEntropy = 0;
			for (String featureValue : featureValueSet) {
				List<Integer> subDataSetList = splitDataSet(dataSetList, i,
						featureValue);
				double probability = subDataSetList.size() * 1.0
						/ dataSetList.size();
				newEntropy += probability * calculateEntropy(subDataSetList);
			}
			// �õ���Ϣ����
			double informationGain = baseEntropy - newEntropy;
			// �õ���Ϣ�������������±�
			if (informationGain > bestInformationGain) {
				bestInformationGain = informationGain;
				bestFeature = temp;
			}
		}
		return bestFeature;
	}

	/**
	 * ��һ�����ݼ����л���
	 * 
	 * @param dataSetList
	 *            �����ֵ����ݼ�
	 * @param featureIndex
	 *            �ڼ�������(�����±�,��0��ʼ)
	 * @param value
	 *            �õ�ĳ������ֵ�����ݼ�
	 * @return
	 */
	public static List<Integer> splitDataSet(List<Integer> dataSetList,
			int featureIndex, String value) {
		List<Integer> resultList = new ArrayList<Integer>();
		for (Integer id : dataSetList) {
			if (tableMap.get(id).get(featureIndex).equals(value)) {
				resultList.add(id);
			}
		}
		return resultList;
	}

	/**
	 * ������
	 * 
	 * @param dataSetList
	 * @return
	 */
	public static double calculateEntropy(List<Integer> dataSetList) {
		if (dataSetList == null || dataSetList.size() <= 0) {
			return 0;
		}
		// �õ����
		int resultIndex = tableMap.get(dataSetList.get(0)).size() - 1;
		Map<String, Integer> valueMap = new HashMap<String, Integer>();
		for (Integer id : dataSetList) {
			String value = tableMap.get(id).get(resultIndex);
			Integer num = valueMap.get(value);
			if (num == null || num == 0) {
				num = 0;
			}
			valueMap.put(value, num + 1);
		}
		double entropy = 0;
		for (Map.Entry<String, Integer> entry : valueMap.entrySet()) {
			double prob = entry.getValue() * 1.0 / dataSetList.size();
			entropy -= prob * Math.log10(prob) / Math.log10(2);
		}
		return entropy;
	}

	/**
	 * ��ʼ������
	 * 
	 * @param file
	 */
	public static void readOriginalData(File file) {
		int index = 0;
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line;
			while ((line = br.readLine()) != null) {
				// �õ���������
				if (line.startsWith("@feature")) {
					line = br.readLine();
					String[] row = line.split(",");
					for (String s : row) {
						featureList.add(s.trim());
					}
				} else if (line.startsWith("@data")) {
					while ((line = br.readLine()) != null) {
						if (line.equals("")) {
							continue;
						}
						String[] row = line.split(",");
						if (row.length != featureList.size()) {
							throw new Exception("�б����ݺ�������Ŀ��һ��");
						}
						List<String> tempList = new ArrayList<String>();
						for (String s : row) {
							if (s.trim().equals("")) {
								throw new Exception("�б����ݲ���Ϊ��");
							}
							tempList.add(s.trim());
						}
						tableMap.put(index++, tempList);
					}

					// ����tableMap�õ�����ֵ�б�
					Map<Integer, Set<String>> valueSetMap = new HashMap<Integer, Set<String>>();
					for (int i = 0; i < featureList.size(); i++) {
						valueSetMap.put(i, new HashSet<String>());
					}
					for (Map.Entry<Integer, List<String>> entry : tableMap
							.entrySet()) {
						List<String> dataList = entry.getValue();
						for (int i = 0; i < dataList.size(); i++) {
							valueSetMap.get(i).add(dataList.get(i));
						}
					}
					for (Map.Entry<Integer, Set<String>> entry : valueSetMap
							.entrySet()) {
						List<String> valueList = new ArrayList<String>();
						for (String s : entry.getValue()) {
							valueList.add(s);
						}
						featureValueTableList.add(valueList);
					}
				} else {
					continue;
				}
			}
			br.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// �ѽ��д��xml�ļ�
	public static void writeToXML(Node node, String filename) {
		try {
			// ����xml
			JAXBContext context = JAXBContext.newInstance(Node.class);
			Marshaller marshaller = context.createMarshaller();

			File file = new File(filename);
			if (file.exists() == false) {
				if (file.getParent() == null) {
					file.getParentFile().mkdirs();
				}
				file.createNewFile();
			}

			marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8"); // ���ñ����ַ���
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true); // ��ʽ��XML������з��к�����

			marshaller.marshal(node, System.out); // ��ӡ������̨

			FileOutputStream fos = new FileOutputStream(file);
			marshaller.marshal(node, fos);
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
