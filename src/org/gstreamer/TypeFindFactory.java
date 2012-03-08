package org.gstreamer;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.gstreamer.lowlevel.GlibAPI;
import org.gstreamer.lowlevel.GlibAPI.GList;
import org.gstreamer.lowlevel.GstNative;
import org.gstreamer.lowlevel.GstTypeFindFactoryAPI;

/**
 *
 * @author DrLabman
 */
public class TypeFindFactory extends PluginFeature {
	private static final Logger logger = Logger.getLogger(ElementFactory.class.getName());
    private static final Level DEBUG = Level.FINE;
	
	public static final String GTYPE_NAME = "GstTypeFindFactory";
	
	private static interface API extends GstTypeFindFactoryAPI, GlibAPI {};
	private static final API gst = GstNative.load(API.class);
	
	public TypeFindFactory(Initializer init){
		super(init);
		logger.entering("TypeFindFactory", "<init>", new Object[] { init });
	}
	
	public static List<TypeFindFactory> getList(){
		logger.entering("TypeFindFactory", "getList");
		GList glist = gst.gst_type_find_factory_get_list();
		logger.log(DEBUG, "gst.gst_type_find_factory_get_list returned: " + glist);
        List<TypeFindFactory> typeFindFactories = new ArrayList<TypeFindFactory>();
        GList next = glist;
        while (next != null) {
            if (next.data != null) {
                typeFindFactories.add(GstObject.objectFor(next.data, TypeFindFactory.class, true, true));
            }
            next = next.next();
        }
        return typeFindFactories;

	}
	
	public String[] getExtentions(){
		logger.entering("TypeFindFactory", "getExtentions");
		return  gst.gst_type_find_factory_get_extensions(this);
	}
}
