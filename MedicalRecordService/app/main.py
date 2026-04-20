from fastapi import FastAPI

app = FastAPI(title="MedicalRecordService")

@app.get("/")
def root():
    return {"message": "MedicalRecordService running"}