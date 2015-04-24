/**
 * Example written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/29194405/html-to-pdf-with-base64-image-throw-filenotfound
 */
package sandbox.xmlworker;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.codec.Base64;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.AbstractImageProvider;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ParseHtml4 {
    
    class Base64ImageProvider extends AbstractImageProvider {
        
        @Override
        public Image retrieve(String src) {
            int pos = src.indexOf("base64,");
            try {
                if (src.startsWith("data") && pos > 0) {
                    byte[] img = Base64.decode(src.substring(pos + 7));
                    return Image.getInstance(img);
                }
                else {
                    return Image.getInstance(src);
                }
            } catch (BadElementException ex) {
                return null;
            } catch (IOException ex) {
                return null;
            }
        }

        @Override
        public String getImageRootPath() {
            return null;
        }
    }
    
    public static final String DEST = "results/xmlworker/html_4.pdf";
    
    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ParseHtml4().createPdf(DEST);
    }
    
    public void createPdf(String file) throws IOException, DocumentException {
        String str = "<html><head><title>Test PDF</title></head><body><div>" + 
            "<img src=\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAc4AAABQCAYAAACQ/ZU3AAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyJpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMy1jMDExIDY2LjE0NTY2MSwgMjAxMi8wMi8wNi0xNDo1NjoyNyAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENTNiAoV2luZG93cykiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6MjRGMzU1Qjk5RjFFMTFFNEE2NzA4QzlBNERCRTcxRTUiIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6MjRGMzU1QkE5RjFFMTFFNEE2NzA4QzlBNERCRTcxRTUiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDoyNEYzNTVCNzlGMUUxMUU0QTY3MDhDOUE0REJFNzFFNSIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDoyNEYzNTVCODlGMUUxMUU0QTY3MDhDOUE0REJFNzFFNSIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PkQbS2MAABpBSURBVHja7F0JuBTV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\" />" +
            "</div><div>Hello world</div></body></html>";

        
                // step 1
        Document document = new Document();
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
        // step 3
        document.open();
        // step 4

        // CSS
        CSSResolver cssResolver =
                XMLWorkerHelper.getInstance().getDefaultCssResolver(true);
        
        // HTML
        HtmlPipelineContext htmlContext = new HtmlPipelineContext(null);
        htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
        htmlContext.setImageProvider(new Base64ImageProvider());
        
        // Pipelines
        PdfWriterPipeline pdf = new PdfWriterPipeline(document, writer);
        HtmlPipeline html = new HtmlPipeline(htmlContext, pdf);
        CssResolverPipeline css = new CssResolverPipeline(cssResolver, html);
        
        // XML Worker
        XMLWorker worker = new XMLWorker(css, true);
        XMLParser p = new XMLParser(worker);
        p.parse(new ByteArrayInputStream(str.getBytes()));
        
        // step 5
        document.close();
    }
}
