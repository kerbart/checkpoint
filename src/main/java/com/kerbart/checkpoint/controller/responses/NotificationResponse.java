package com.kerbart.checkpoint.controller.responses;

import java.util.List;

import com.kerbart.checkpoint.model.UtilisateurNotification;

public class NotificationResponse {
	
	List<UtilisateurNotification> notificationPatients;
	List<UtilisateurNotification> notificationCommentaire;
	List<UtilisateurNotification> notificationOrdonnance;
	ErrorCode error;
	
	public List<UtilisateurNotification> getNotificationPatients() {
		return notificationPatients;
	}
	public void setNotificationPatients(List<UtilisateurNotification> notificationPatients) {
		this.notificationPatients = notificationPatients;
	}
	public List<UtilisateurNotification> getNotificationCommentaire() {
		return notificationCommentaire;
	}
	public void setNotificationCommentaire(List<UtilisateurNotification> notificationCommentaire) {
		this.notificationCommentaire = notificationCommentaire;
	}
	public List<UtilisateurNotification> getNotificationOrdonnance() {
		return notificationOrdonnance;
	}
	public void setNotificationOrdonnance(List<UtilisateurNotification> notificationOrdonnance) {
		this.notificationOrdonnance = notificationOrdonnance;
	}
	public ErrorCode getError() {
		return error;
	}
	public void setError(ErrorCode error) {
		this.error = error;
	}
}
