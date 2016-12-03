package com.spaculus.beans;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class TriviaGame implements Parcelable {

	private String trivia_id = "";
	private String question = "";
	private ArrayList<String> answerOptionsList = null;
	private String answer = "";
	private long timeTaken = 0;

	public TriviaGame() {
	}

	public String getTrivia_id() {
		return trivia_id;
	}

	public void setTrivia_id(String trivia_id) {
		this.trivia_id = trivia_id;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public ArrayList<String> getAnswerOptionsList() {
		return answerOptionsList;
	}

	public void setAnswerOptionsList(ArrayList<String> answerOptionsList) {
		this.answerOptionsList = answerOptionsList;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public long getTimeTaken() {
		return timeTaken;
	}

	public void setTimeTaken(long timeTaken) {
		this.timeTaken = timeTaken;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeString(trivia_id);
		parcel.writeString(question);
		parcel.writeList(answerOptionsList);
		parcel.writeString(answer);
		parcel.writeLong(timeTaken);
	}

	public static final Parcelable.Creator<TriviaGame> CREATOR = new Parcelable.Creator<TriviaGame>() {
		public TriviaGame createFromParcel(Parcel in) {
			return new TriviaGame(in);
		}

		public TriviaGame[] newArray(int size) {
			return new TriviaGame[size];
		}
	};

	@SuppressWarnings("unchecked")
	private TriviaGame(Parcel in) {
		trivia_id = in.readString();
		question = in.readString();
		answerOptionsList = in.readArrayList(String.class.getClassLoader());
		answer = in.readString();
		timeTaken = in.readLong();
	}

	public static Parcelable.Creator<TriviaGame> getCreator() {
		return CREATOR;
	}
}
