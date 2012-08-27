import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoField;
import com.sap.conn.jco.JCoFieldIterator;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoTable;

// Jco���b�s���O�p�̃N���X
public class JcoRfcCall {
    
    // �ڑ���
    public JCoDestination destination;
    
    // ���s�t�@���N�V����
    public JCoFunction function;

    /**
     * �R���X�g���N�^ �v���p�e�B�t�@�C�����ƁA�Ăяo���t�@���N�V���������󂯎���Đڑ����͂� ���|�W�g������t�@���N�V�����̎擾�܂ōs��
     * 
     * @param propKey
     * @param functionName
     * @throws JCoException
     */
    public JcoRfcCall(JCoDestination destination, String functionName) throws JCoException {
        this.destination = destination;
        this.function = this.destination.getRepository().getFunction(functionName);

    }

    /**
     * �t�@���N�V�������s�p
     * 
     * @throws JCoException
     */
    public void execute() throws JCoException {
        this.function.execute(this.destination);
    }

    /**
     * �C���|�[�g�p�����[�^�̃Z�b�^�[ 
     * JCoParameterList�����b�s���O���邽�߂̃��\�b�h
     *
     * @param map
     */
    public void setImportParameter(Map<String, String> map) {
        JCoParameterList listParams = this.function.getImportParameterList();
        Set keySet = map.keySet();
        Iterator keyIte = keySet.iterator();
        while (keyIte.hasNext()) {
            String key = (String) keyIte.next();
            String value = map.get(key);
            listParams.setValue(key, value);
        }
    }

    /**
     * �e�[�u���p�����[�^�̃Q�b�^�[ 
     * JCoTable�����b�s���O���邽�߂̃��\�b�h
     * 
     * @param tableName
     * @return List<Map>
     */
    public List<Map<String, String>> getTableParameter(String tableName) {
        List<Map<String, String>> result = new ArrayList<Map<String, String>>();
        JCoTable tableList = this.function.getTableParameterList().getTable(tableName);
        if (tableList.getNumRows() > 0) {
            do {
                Map<String, String> map = new HashMap<String, String>();
                for (JCoFieldIterator fI = tableList.getFieldIterator(); fI.hasNextField();) {
                    JCoField tabField = fI.nextField();
                    map.put(tabField.getName(), tabField.getString());

                }
                result.add(map);
            } while (tableList.nextRow() == true);
        }
        return result;
    }

    /**
     * �e�[�u���p�����[�^�̃Z�b�^�[
     * JCoTable�����b�s���O���邽�߂̃��\�b�h
     *
     * @param tableName
     * @param records
     */
    public void setTableParameter(String tableName, List<Map<String, String>> records) {
        JCoTable tableList = this.function.getTableParameterList().getTable(tableName);
        tableList.appendRows(records.size());
        for (Map<String, String> record : records) {
            Set keySet = record.keySet();
            Iterator keyIte = keySet.iterator();
            while (keyIte.hasNext()) {
                String key = (String) keyIte.next();
                String value = record.get(key);
                tableList.setValue(key, value);
            }
            tableList.nextRow();
        }
    }

    /**
     * �G�N�X�|�[�g�p�����[�^�̃Q�b�^�[�i���Ԃ񂱂�Ȋ����ōs����͂��B�������ĂȂ����\�b�h�Ȃ̂Ń_���������炲�߂�Ȃ����j 
     * JCoParameterList�����b�s���O���邽�߂̃��\�b�h
     * 
     * @return Map
     */
    public Map<String, String> getExportParameter() {
        Map<String, String> result = new HashMap<String, String>();
        JCoParameterList listParams = this.function.getExportParameterList();
        for (JCoFieldIterator fI = listParams.getFieldIterator(); fI.hasNextField();) {
            JCoField field = fI.nextField();
            result.put(field.getName(), field.getString());
        }
        return result;
    }
}