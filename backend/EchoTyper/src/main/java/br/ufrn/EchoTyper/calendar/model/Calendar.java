package br.ufrn.EchoTyper.calendar.model;

import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tb_calendar")
public class Calendar {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_meeting")
	private Long id;

	@NotNull(message = "Title is required")
	@Size(min = 1, max = 50, message = "Title must be between 1 and 50 characters")
	@Column(nullable = false, unique = false, name = "str_title")
	private String title;

	@Column(nullable = true, name = "str_description")
	private String description;

	@NotNull(message = "Date is required")
	@Column(nullable = false, unique = false, name = "str_date")
	private LocalDate date;

	public Calendar() {
	}

	public Calendar(Long id, String title, String description, LocalDate date) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.date = date;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}
}
