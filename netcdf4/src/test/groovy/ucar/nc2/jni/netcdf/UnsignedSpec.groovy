package ucar.nc2.jni.netcdf

import spock.lang.Specification
import spock.lang.Unroll
import ucar.ma2.Array
import ucar.ma2.Section
import ucar.nc2.Attribute
import ucar.nc2.NetcdfFile
import ucar.nc2.Variable

/**
 * Tests that Nc4Iosp can read unsigned data.
 *
 * @author cwardgar
 * @since 2017-03-27
 */
class UnsignedSpec extends Specification {
    /**
     * Demonstrates bug from http://www.unidata.ucar.edu/mailing_lists/archives/netcdf-java/2017/msg00012.html
     * Prior to fix, this test would fail for 'u_short', 'u_int', and 'u_long' variables with
     * "Unknown userType == 8", "Unknown userType == 9", and "Unknown userType == 11" errors respectively.
     */
    @Unroll  // Report iterations of method independently.
    def "Nc4Iosp.readDataSection() can read '#varName' variables"() {
        setup: "locate test file"
        File file = new File(this.class.getResource("unsigned.nc4").toURI())
        assert file.exists()
        
        and: "open it as a NetcdfFile using Nc4Iosp"
        NetcdfFile ncFile = NetcdfFile.open(file.absolutePath, Nc4Iosp.class.canonicalName, -1, null, null)
        
        and: "grab the Nc4Iosp instance within so that we can test Nc4Iosp.readDataSection()"
        Nc4Iosp nc4Iosp = ncFile.iosp as Nc4Iosp
        
        when: "read all of var's data using readDataSection()"
        Variable var = ncFile.findVariable(varName)
        Nc4Iosp.Vinfo vinfo = var.SPobject as Nc4Iosp.Vinfo
        Array array = nc4Iosp.readDataSection(vinfo.g4.grpid, vinfo.varid, vinfo.typeid, var.shapeAsSection);
        
        then: "actual data equals expected data"
        array.storage == expectedData
    
        cleanup: "close NetcdfFile"
        ncFile?.close()
        
        where: "data are too big for their type. Overflow expected because Java doesn't support unsigned types"
        varName << [ "u_byte", "u_short", "u_int", "u_long" ]
        expectedData << [
                [(1  << 7),  (1  << 7)  + 1, (1  << 7)  + 2] as byte[],  // Will overflow to [-128, -127, -126]
                [(1  << 15), (1  << 15) + 1, (1  << 15) + 2] as short[],
                [(1  << 31), (1  << 31) + 1, (1  << 31) + 2] as int[],
                [(1L << 63), (1L << 63) + 1, (1L << 63) + 2] as long[]
        ];
    }
}