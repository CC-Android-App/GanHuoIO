package ren.solid.library.imageloader;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.stream.StreamModelLoader;

import java.io.IOException;
import java.io.InputStream;

import ren.solid.library.R;
import ren.solid.library.SolidApplication;
import ren.solid.library.utils.NetworkUtil;
import ren.solid.library.utils.SettingUtils;

import static java.lang.System.load;


/**
 * Created by _SOLID
 * Date:2016/5/13
 * Time:10:27
 */
public class GlideImageLoaderProvider implements IImageLoaderProvider {
    @Override
    public void loadImage(ImageRequest request) {
        Context context = SolidApplication.getInstance();
        request.setPlaceHolder(R.drawable.default_load_img);
        if (!SettingUtils.getOnlyWifiLoadImage()) {
            loadNormal(context, request);
        } else {
            if (NetworkUtil.isWifiConnected(context)) {
                loadNormal(context, request);
            } else {
                loadCache(context, request);
            }
        }

    }

    private void loadNormal(Context ctx, ImageRequest img) {
        Glide.with(ctx)
                .load(img.getUrl())
                .placeholder(img.getPlaceHolder())
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(img.getImageView());
    }

    private void loadCache(Context ctx, ImageRequest img) {
        Glide.with(ctx)
                .using(new StreamModelLoader<String>() {
                    @Override
                    public DataFetcher<InputStream> getResourceFetcher(final String model, int i, int i1) {
                        return new DataFetcher<InputStream>() {
                            @Override
                            public InputStream loadData(Priority priority) throws Exception {
                                throw new IOException();
                            }

                            @Override
                            public void cleanup() {

                            }

                            @Override
                            public String getId() {
                                return model;
                            }

                            @Override
                            public void cancel() {

                            }
                        };
                    }
                })
                .load(img.getUrl())
                .placeholder(img.getPlaceHolder())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(img.getImageView());
    }
}
