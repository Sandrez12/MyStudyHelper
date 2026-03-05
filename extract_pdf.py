import PyPDF2

path = r'C:\Users\green\Downloads\PrácticaMVVM.pdf'
reader = PyPDF2.PdfReader(path)
for i, page in enumerate(reader.pages):
    text = page.extract_text()
    print(f"=== Page {i+1} ===")
    print(text[:1500])
