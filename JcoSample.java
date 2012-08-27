import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;

public class JcoSample {
	public static void main(String[] args) throws JCoException {
		// destination�̎擾
	        JCoDestination destination = JCoDestinationManager.getDestination("ABAP_AS_WITH_POOL");

		JcoRfcCall jcoRfcCall = new JcoRfcCall(destination, "RFC_READ_TABLE");
		
		// �C���|�[�g�p�����[�^��ݒ�
		jcoRfcCall.setImportParameter(new HashMap<String, String>() {{
			put("QUERY_TABLE", "T001W"); // �e�[�u����
			put("DELIMITER", "\t"); // �f�[�^�̃f���~�^
			put("NO_DATA", " "); // �f�[�^���Ȃ��ꍇ�̈���
			put("ROWSKIPS", "0"); // �X�L�b�v����s��
			put("ROWCOUNT", "10"); // �擾����s��
		}});
		// �e�[�u���p�����[�^��ݒ�
		List<Map<String, String>> tableParam = new ArrayList<Map<String, String>>();
		tableParam.add(new HashMap<String, String>() {{ put("FIELDNAME", "MANDT"); }});
		tableParam.add(new HashMap<String, String>() {{ put("FIELDNAME", "WERKS"); }});
		tableParam.add(new HashMap<String, String>() {{ put("FIELDNAME", "NAME1"); }});
		jcoRfcCall.setTableParameter("FIELDS", tableParam);
		
		// ���s
		jcoRfcCall.execute();
		
		// ���ʏ���
		List<Map<String, String>> headers = jcoRfcCall.getTableParameter("FIELDS"); // Select�Ώۂ̗�
		List<Map<String, String>> records = jcoRfcCall.getTableParameter("DATA"); // �f�[�^

		// �w�b�_�[���o��
		for(Map<String, String> header: headers) {
			System.out.print(header.get("FIELDNAME") + "\t");
		}
		System.out.print("\n");
		// �f�[�^���o��
		for(Map<String, String> record: records) {
			System.out.println(record.get("WA")); // RFC_READ_TABLE��WA�Ƀf�[�^��DELIMITER�ŋ�؂��Ċi�[�����
		}
		 
		return;
	}
}