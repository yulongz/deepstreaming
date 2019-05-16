package hbase;

import org.junit.Test;

import java.io.IOException;

import static hbase.PutMappingData.putData;

public class PutMappingDataTest {

    @Test
    public void putMappingDataTest() throws IOException {
        putData("DCS:AI:10HLF10CF101", "AE587FP1HPA01HP001ZZ00J0", "#1送风机风量1", "2954", "A", "01", "0号", "00");
        putData("DCS:AI:10HLF10CF101F", "AE587FP1HPA01HP001ZZ01J0", "#1送风机风量1", "2955", "A", "01", "1号", "01");
        putData("DCS:AI:10HLF10CF102", "AE587FP1HPA01HP001ZZ02J0", "#1送风机风量1", "2956", "A", "01", "2号", "02");
        putData("DCS:AI:10HLF10CF102F", "AE587FP1HPA01HP001ZZ03J0", "#1送风机风量1", "2957", "A", "01", "3号", "03");
        putData("DCS:AI:10HLF10CF103", "AE587FP1HPA01HP001ZZ04J0", "#1送风机风量1", "2958", "A", "01", "4号", "04");
        putData("DCS:AI:10HLF10CF103F", "AE587FP1HPA01HP001ZZ05J0", "#1送风机风量1", "2959", "A", "01", "5号", "05");

        putData("DCS:AI:10HLF20CF101", "AE587FP1HPA02HP001ZZ00J0", "#1送风机风量1", "2987", "B", "02", "0号", "00");
        putData("DCS:AI:10HLF20CF101F", "AE587FP1HPA02HP001ZZ01J0", "#1送风机风量1", "2988", "B", "02", "1号", "01");
        putData("DCS:AI:10HLF20CF102", "AE587FP1HPA02HP001ZZ02J0", "#1送风机风量1", "2989", "B", "02", "2号", "02");
        putData("DCS:AI:10HLF20CF102F", "AE587FP1HPA02HP001ZZ03J0", "#1送风机风量1", "2990", "B", "02", "3号", "03");
        putData("DCS:AI:10HLF20CF103", "AE587FP1HPA02HP001ZZ04J0", "#1送风机风量1", "2991", "B", "02", "4号", "04");
        putData("DCS:AI:10HLF20CF103F", "AE587FP1HPA02HP001ZZ05J0", "#1送风机风量1", "2992", "B", "02", "5号", "05");

    }

}
