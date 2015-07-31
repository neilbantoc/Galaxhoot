/*
 * GameOverActivity.java
 * 
 * @author Neil Bantoc and James Plaras 
 */
package project161.galaxhoot;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

// TODO: Auto-generated Javadoc
/**
 * The Class GameOverActivity.
 */
public class GameOverActivity extends Activity implements OnClickListener {

	/** The view_mode. */
	private int view_mode = 0;

	/** The Constant GAME_OVER. */
	public static final String GAME_OVER = "game_over";

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.game_over);

		View v = this.findViewById(android.R.id.content);
		v.setOnClickListener(this);

		view_mode = this.getIntent().getIntExtra("view_mode",
				Constants.TOP_VIEW);

		TextView tv = (TextView) this.findViewById(R.id.result);

		tv.setText(this.getIntent().getStringExtra("result"));

		tv = (TextView) this.findViewById(R.id.score);
		int score = this.getIntent().getIntExtra("score", 0);
		tv.setText("Score: " + score);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		Intent intent = new Intent(this, MainActivity.class);

		intent.putExtra("view_mode", view_mode);

		this.startActivity(intent);
		this.finish();
	}

}
