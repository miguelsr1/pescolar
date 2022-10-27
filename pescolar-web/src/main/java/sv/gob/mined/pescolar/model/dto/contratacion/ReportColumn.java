/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.pescolar.model.dto.contratacion;

import org.apache.poi.hssf.usermodel.HSSFFont;

/**
 *
 * @author oamartinez
 */
public class ReportColumn {
    private String m_method;
    private String m_header;
    private Bean2Excel.FormatType m_type;
    private HSSFFont m_font;
    private Short m_color;
 
    public ReportColumn(String method, String header, Bean2Excel.FormatType type,
            HSSFFont font, Short color) {
        this.m_method = method;
        this.m_header = header;
        this.m_type = type;
        this.m_font = font;
        this.m_color = color;
    }
    public ReportColumn(String method, String header, Bean2Excel.FormatType type,
            HSSFFont font) {
        this(method, header, type, font, null);
    }
    public ReportColumn(String method, String header, Bean2Excel.FormatType type,
            Short color) {
        this(method, header, type, null, color);
    }
 
    public ReportColumn(String method, String header, Bean2Excel.FormatType type) {
        this(method, header, type, null, null);
    }
 
    public String getMethod() {
        return m_method;
    }
 
    public void setMethod(String method) {
        this.m_method = method;
    }
 
    public String getHeader() {
        return m_header;
    }
 
    public void setHeader(String header) {
        this.m_header = header;
    }
 
    public Bean2Excel.FormatType getType() {
        return m_type;
    }
 
    public void setType(Bean2Excel.FormatType type) {
        this.m_type = type;
    }
 
    public HSSFFont getFont() {
        return m_font;
    }
 
    public void setFont(HSSFFont m_font) {
        this.m_font = m_font;
    }
 
    public Short getColor() {
        return m_color;
    }
 
    public void setColor(Short m_color) {
        this.m_color = m_color;
    }
}
