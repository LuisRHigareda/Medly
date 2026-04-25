"""
pdf_generator.py
----------------
Utility module for generating medical prescription PDFs.

Uses fpdf2 to build a structured, print-ready PDF document containing:
- Patient information (id, blood type, weight, height, notes)
- Diagnoses (name, date, status)
- Allergies (allergen, reaction, severity)
- Prescribed medications (name, dose, frequency, treatment dates)
- Medical procedures (name, date, notes)

Entry point: build_prescription_pdf(record) -> bytes
"""

from fpdf import FPDF
from app.models.medical_record import MedicalRecord


# --- Layout constants ---
MARGIN = 15          # left/right page margin in mm
LINE_HEIGHT = 7      # standard line height in mm
SECTION_GAP = 4      # vertical space before each section header
PAGE_WIDTH = 210     # A4 width in mm
CONTENT_WIDTH = PAGE_WIDTH - 2 * MARGIN  # usable width


def _section_header(pdf: FPDF, title: str) -> None:
    """Draw a filled section header bar with white bold text."""
    pdf.ln(SECTION_GAP)
    pdf.set_fill_color(52, 73, 94)    # dark blue-grey
    pdf.set_text_color(255, 255, 255) # white
    pdf.set_font("Helvetica", style="B", size=11)
    pdf.cell(CONTENT_WIDTH, 8, title, fill=True, ln=True)
    pdf.set_text_color(0, 0, 0)       # reset to black
    pdf.set_font("Helvetica", size=10)
    pdf.ln(1)


def _label_value(pdf: FPDF, label: str, value: str) -> None:
    """Print a bold label followed by its value on the same line."""
    pdf.set_font("Helvetica", style="B", size=10)
    pdf.cell(45, LINE_HEIGHT, f"{label}:", ln=False)
    pdf.set_font("Helvetica", size=10)
    pdf.cell(0, LINE_HEIGHT, value, ln=True)


def _row(pdf: FPDF, text: str) -> None:
    """Print a plain indented row (used inside lists)."""
    pdf.set_font("Helvetica", size=10)
    pdf.cell(5, LINE_HEIGHT, "", ln=False)   # indent
    pdf.multi_cell(CONTENT_WIDTH - 5, LINE_HEIGHT, text)


def build_prescription_pdf(record: MedicalRecord) -> bytes:
    """
    Generate a prescription PDF for the given medical record.

    Loads all related data (blood type, diagnoses, allergies,
    prescribed medications with medication names, and procedures)
    from the already-loaded SQLAlchemy model instance.

    Returns the PDF as raw bytes so it can be sent directly as
    an HTTP response without writing a file to disk.
    """
    pdf = FPDF()
    pdf.set_margins(MARGIN, MARGIN, MARGIN)
    pdf.add_page()
    pdf.set_auto_page_break(auto=True, margin=MARGIN)

    # ------------------------------------------------------------------ #
    # Header
    # ------------------------------------------------------------------ #
    pdf.set_font("Helvetica", style="B", size=16)
    pdf.set_text_color(52, 73, 94)
    pdf.cell(0, 10, "Medical Prescription", align="C", ln=True)

    pdf.set_font("Helvetica", size=9)
    pdf.set_text_color(100, 100, 100)
    consultation_date = record.created_at.strftime("%B %d, %Y") if record.created_at else "N/A"
    pdf.cell(0, 6, f"Consultation date: {consultation_date}", align="C", ln=True)

    pdf.set_text_color(0, 0, 0)
    pdf.ln(2)

    # Horizontal divider
    pdf.set_draw_color(52, 73, 94)
    pdf.set_line_width(0.5)
    pdf.line(MARGIN, pdf.get_y(), PAGE_WIDTH - MARGIN, pdf.get_y())
    pdf.ln(3)

    # ------------------------------------------------------------------ #
    # Patient Information
    # ------------------------------------------------------------------ #
    _section_header(pdf, "  Patient Information")

    _label_value(pdf, "Patient ID", str(record.patient_id))

    blood_type_name = record.blood_type.type_name if record.blood_type else "Not registered"
    _label_value(pdf, "Blood type", blood_type_name)

    weight_str = f"{record.weight} kg" if record.weight is not None else "Not registered"
    height_str = f"{record.height} m" if record.height is not None else "Not registered"
    _label_value(pdf, "Weight", weight_str)
    _label_value(pdf, "Height", height_str)

    notes_str = record.notes if record.notes else "None"
    _label_value(pdf, "General notes", notes_str)

    # ------------------------------------------------------------------ #
    # Diagnoses
    # ------------------------------------------------------------------ #
    _section_header(pdf, "  Diagnoses")

    if record.diagnoses:
        for diagnosis in record.diagnoses:
            date_str = diagnosis.diagnosed_date.strftime("%Y-%m-%d") if diagnosis.diagnosed_date else "N/A"
            status_str = diagnosis.status.status_name if diagnosis.status else "No status"
            _row(pdf, f"- {diagnosis.name}  |  Date: {date_str}  |  Status: {status_str}")
    else:
        _row(pdf, "No diagnoses registered.")

    # ------------------------------------------------------------------ #
    # Allergies
    # ------------------------------------------------------------------ #
    _section_header(pdf, "  Allergies")

    if record.allergies:
        for allergy in record.allergies:
            reaction_str = allergy.reaction if allergy.reaction else "N/A"
            severity_str = allergy.severity if allergy.severity else "N/A"
            _row(pdf, f"- {allergy.allergen}  |  Reaction: {reaction_str}  |  Severity: {severity_str}")
    else:
        _row(pdf, "No allergies registered.")

    # ------------------------------------------------------------------ #
    # Prescribed Medications
    # ------------------------------------------------------------------ #
    _section_header(pdf, "  Prescribed Medications")

    if record.prescribed_medications:
        for pm in record.prescribed_medications:
            med_name = pm.medication.name if pm.medication else f"Medication ID {pm.medication_id}"
            dose_str = f"{pm.dose} mg" if pm.dose is not None else "N/A"
            freq_str = f"{pm.frequency}x/day" if pm.frequency is not None else "N/A"
            start_str = pm.start_date.strftime("%Y-%m-%d") if pm.start_date else "N/A"
            end_str = pm.end_date.strftime("%Y-%m-%d") if pm.end_date else "Ongoing"
            _row(pdf, f"- {med_name}  |  Dose: {dose_str}  |  Frequency: {freq_str}  |  {start_str} to {end_str}")
    else:
        _row(pdf, "No medications prescribed.")

    # ------------------------------------------------------------------ #
    # Medical Procedures
    # ------------------------------------------------------------------ #
    _section_header(pdf, "  Medical Procedures")

    if record.procedures:
        for procedure in record.procedures:
            date_str = procedure.date.strftime("%Y-%m-%d") if procedure.date else "N/A"
            notes_str = procedure.notes if procedure.notes else "No notes"
            _row(pdf, f"- {procedure.name}  |  Date: {date_str}")
            # Print notes on a second indented line if present
            if procedure.notes:
                pdf.set_font("Helvetica", style="I", size=9)
                pdf.set_text_color(80, 80, 80)
                pdf.cell(10, LINE_HEIGHT, "", ln=False)  # deeper indent
                pdf.multi_cell(CONTENT_WIDTH - 10, LINE_HEIGHT, f"Notes: {notes_str}")
                pdf.set_text_color(0, 0, 0)
    else:
        _row(pdf, "No procedures registered.")

    # ------------------------------------------------------------------ #
    # Footer
    # ------------------------------------------------------------------ #
    pdf.ln(6)
    pdf.set_draw_color(52, 73, 94)
    pdf.line(MARGIN, pdf.get_y(), PAGE_WIDTH - MARGIN, pdf.get_y())
    pdf.ln(3)
    pdf.set_font("Helvetica", style="I", size=8)
    pdf.set_text_color(130, 130, 130)
    pdf.cell(0, 5, "Generated by Medly - MedicalRecordService", align="C", ln=True)

    # Return raw bytes (no temp file needed)
    return bytes(pdf.output())
