package org.gstreamer.lowlevel;

import org.gstreamer.Caps;
import org.gstreamer.TypeFindFactory;
import org.gstreamer.lowlevel.GlibAPI.GList;

/**
 *
 * @author DrLabman
 */
public interface GstTypeFindFactoryAPI extends com.sun.jna.Library {
	GstTypeFindFactoryAPI GSTTYPEFINDFACTORY_API = GstNative.load(GstTypeFindFactoryAPI.class);
	
	GType gst_type_find_factory_get_type();
	
	GList gst_type_find_factory_get_list();
	String[] gst_type_find_factory_get_extensions(TypeFindFactory factory);
	Caps gst_type_find_factory_get_caps(TypeFindFactory factory);
}