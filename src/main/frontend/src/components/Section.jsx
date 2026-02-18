function Section({ title, subtitle, children }) {
  return (
    <section className="section">
      <div className="section-heading">
        <h2>{title}</h2>
        {subtitle ? <p className="section-subtitle">{subtitle}</p> : null}
      </div>
      {children}
    </section>
  );
}

export default Section;
