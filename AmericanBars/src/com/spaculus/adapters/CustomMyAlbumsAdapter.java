package com.spaculus.adapters;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.spaculus.americanbars.R;
import com.spaculus.americanbars.fragments.FragmentMyAlbums;
import com.spaculus.beans.Album;
import com.spaculus.helpers.Utils;

public class CustomMyAlbumsAdapter extends ArrayAdapter<Album> {

	private Context context;
	private List<Album> albumList = null;
	private FragmentMyAlbums objFragmentMyAlbums = null;

	public CustomMyAlbumsAdapter(Context c, int resourceId, List<Album> items, FragmentMyAlbums fragmentMyAlbums) {
		super(c, resourceId, items);
		this.context = c;
		this.albumList = items;
		this.objFragmentMyAlbums = fragmentMyAlbums;
	}

	//  Private View Holder Class
	private class ViewHolder {
		//  Mapping of all the views
		TextView tvTitleContent;
		TextView tvDateContent;
		TextView tvStatusContent;
		CheckBox checkBoxMyAlbum;
		ImageView ivEditAlbum;
		ImageView ivDeleteAlbum;
		ImageView ivViewAlbum;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return albumList.size();
	}

	@SuppressLint("InflateParams") 
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		try {
			ViewHolder holder = null;
			
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.fragment_my_albums_list_item, null);

				// well set up the ViewHolder
				holder = new ViewHolder();
				
				// Mapping of all the views
				holder.tvTitleContent = (TextView) convertView.findViewById(R.id.tvTitleContent);
				holder.tvDateContent = (TextView) convertView.findViewById(R.id.tvDateContent);
				holder.tvStatusContent = (TextView) convertView.findViewById(R.id.tvStatusContent);
				holder.checkBoxMyAlbum = (CheckBox) convertView.findViewById(R.id.checkBoxMyAlbum);
				holder.ivEditAlbum = (ImageView) convertView.findViewById(R.id.ivEditAlbum);
				holder.ivDeleteAlbum = (ImageView) convertView.findViewById(R.id.ivDeleteAlbum);
				holder.ivViewAlbum = (ImageView) convertView.findViewById(R.id.ivViewAlbum);
				
				// store the holder with the view.
				convertView.setTag(holder);
			} 
			else {
				// we've just avoided calling findViewById() on resource every time
				// just use the viewHolder
				holder = (ViewHolder) convertView.getTag();
			}

			//  CheckBox click event
			holder.checkBoxMyAlbum.setOnClickListener( new View.OnClickListener() {
				public void onClick(View v) {
					try {
						CheckBox cb = (CheckBox) v ;
						Album objAlbum = (Album) cb.getTag();  
						//Toast.makeText(context, "Clicked on Checkbox: " + cb.getText() +" is " + cb.isChecked(), Toast.LENGTH_LONG).show();
						objAlbum.setSelected(cb.isChecked());
						//  Now, check need to change the "Select All" button text or not
						objFragmentMyAlbums.checkButtonText();
					} 
					catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			     }  
			});  
			
			//  Edit image functionality
			holder.ivEditAlbum.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					//((TransactionActivity) objFragmentMyAlbums.getActivity()).editAlbum(albumList.get(position).getBar_gallery_id());
					objFragmentMyAlbums.editAlbum(albumList.get(position).getBar_gallery_id());
				}
			});
			
			//  Delete image functionality
			holder.ivDeleteAlbum.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					try {
						//  To delete a single bar
						objFragmentMyAlbums.showAlertDialogMethod(albumList.get(position).getBar_gallery_id());
					} 
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			
			//  View Album click event functionality
			holder.ivViewAlbum.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					try {
						objFragmentMyAlbums.viewAlbumDetails(albumList.get(position).getStatus(), albumList.get(position).getBar_gallery_id(), albumList.get(position).getTitle());
					} 
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			
			//  Now, set the data here.
			
			//  Title
			holder.tvTitleContent.setText(albumList.get(position).getTitle());
			
			//  Date
			holder.tvDateContent.setText(Utils.getInstance().convertDateTime(albumList.get(position).getDate_added()));
			
			//  Status
			holder.tvStatusContent.setText(albumList.get(position).getStatus());

			//  CheckBox Status
			holder.checkBoxMyAlbum.setChecked(albumList.get(position).isSelected());
			holder.checkBoxMyAlbum.setTag(albumList.get(position));
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return convertView;
	}
}


