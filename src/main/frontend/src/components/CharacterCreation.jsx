import { useEffect, useMemo, useState } from "react";
import { factionTone, formatEnum } from "../lib/format";

function CharacterCreation({
  races,
  classes,
  factions,
  onCreatePlayer,
  onRefreshPlayers,
  onLoadRace,
  onLoadClass,
  onLoadFaction,
}) {
  const [name, setName] = useState("");
  const [raceId, setRaceId] = useState("");
  const [classId, setClassId] = useState("");
  const [factionId, setFactionId] = useState("");
  const [raceDetail, setRaceDetail] = useState(null);
  const [classDetail, setClassDetail] = useState(null);
  const [factionDetail, setFactionDetail] = useState(null);
  const [status, setStatus] = useState({ type: "", message: "" });
  const [isSubmitting, setIsSubmitting] = useState(false);

  const selectedRace = useMemo(
    () => races.find((race) => race.id?.toString() === raceId),
    [raceId, races],
  );

  const availableClasses = useMemo(() => {
    if (!selectedRace?.clasesDisponibles?.length) return classes;
    return classes.filter((clase) =>
      selectedRace.clasesDisponibles.includes(clase.nombre),
    );
  }, [classes, selectedRace]);

  useEffect(() => {
    if (!raceId) return;
    onLoadRace(raceId)
      .then(setRaceDetail)
      .catch(() => setRaceDetail(null));
  }, [onLoadRace, raceId]);

  useEffect(() => {
    if (!classId) return;
    onLoadClass(classId)
      .then(setClassDetail)
      .catch(() => setClassDetail(null));
  }, [classId, onLoadClass]);

  useEffect(() => {
    if (!factionId) return;
    onLoadFaction(factionId)
      .then(setFactionDetail)
      .catch(() => setFactionDetail(null));
  }, [factionId, onLoadFaction]);

  useEffect(() => {
    if (!selectedRace) return;
    const matchingFaction = factions.find(
      (faction) => faction.nombre === selectedRace.faccion,
    );
    if (matchingFaction && matchingFaction.id?.toString() !== factionId) {
      setFactionId(matchingFaction.id.toString());
    }
  }, [factionId, factions, selectedRace]);

  useEffect(() => {
    if (!classId) return;
    const stillAvailable = availableClasses.some(
      (clase) => clase.id?.toString() === classId,
    );
    if (!stillAvailable) {
      setClassId("");
    }
  }, [availableClasses, classId]);

  const resetStatus = () => setStatus({ type: "", message: "" });

  const handleSubmit = async (event) => {
    event.preventDefault();
    resetStatus();

    if (!name || !raceId || !classId || !factionId) {
      setStatus({
        type: "error",
        message: "Completa todos los campos para forjar tu héroe.",
      });
      return;
    }

    try {
      setIsSubmitting(true);
      await onCreatePlayer({
        nombre: name,
        razaId: Number(raceId),
        claseId: Number(classId),
        faccionId: Number(factionId),
      });
      setStatus({
        type: "success",
        message: "¡Héroe creado! Tus crónicas han comenzado.",
      });
      setName("");
      setRaceId("");
      setClassId("");
      setFactionId("");
      await onRefreshPlayers();
    } catch (error) {
      setStatus({
        type: "error",
        message: error.message || "No se pudo crear el héroe.",
      });
    } finally {
      setIsSubmitting(false);
    }
  };

  const factionClass = factionTone(selectedRace?.faccion);

  return (
    <div className="grid-two">
      <form
        className={`card form-card ${factionClass}`}
        onSubmit={handleSubmit}
      >
        <h3>Forja del personaje</h3>
        <p className="muted">
          Elige tu linaje, tu camino y el estandarte que jurarás proteger.
        </p>

        <div className="form-grid">
          <label className="form-field">
            <span>Nombre del héroe</span>
            <input
              type="text"
              value={name}
              onChange={(event) => setName(event.target.value)}
              placeholder="Ej: Thalorien"
            />
          </label>

          <label className="form-field">
            <span>Raza</span>
            <select
              value={raceId}
              onChange={(event) => setRaceId(event.target.value)}
            >
              <option value="">Selecciona una raza</option>
              {races.map((race) => (
                <option key={race.id} value={race.id}>
                  {formatEnum(race.nombre)}
                </option>
              ))}
            </select>
          </label>

          <label className="form-field">
            <span>Clase</span>
            <select
              value={classId}
              onChange={(event) => setClassId(event.target.value)}
            >
              <option value="">Selecciona una clase</option>
              {availableClasses.map((clase) => (
                <option key={clase.id} value={clase.id}>
                  {formatEnum(clase.nombre)}
                </option>
              ))}
            </select>
          </label>

          <label className="form-field">
            <span>Facción</span>
            <select
              value={factionId}
              onChange={(event) => setFactionId(event.target.value)}
            >
              <option value="">Selecciona facción</option>
              {factions.map((faction) => (
                <option key={faction.id} value={faction.id}>
                  {formatEnum(faction.nombre)}
                </option>
              ))}
            </select>
          </label>
        </div>

        <button type="submit" disabled={isSubmitting}>
          {isSubmitting ? "Forjando..." : "Crear héroe"}
        </button>

        {status.message ? (
          <div className={`${status.type}-message mt-2`}>{status.message}</div>
        ) : null}
      </form>

      <div className="card preview-card">
        <h3>Juramento del campeón</h3>
        <div className="preview-content">
          <div>
            <p className="muted">Nombre</p>
            <p className="preview-value">{name || "Sin nombre"}</p>
          </div>
          <div>
            <p className="muted">Raza</p>
            <p className="preview-value">
              {formatEnum(selectedRace?.nombre) || "Sin definir"}
            </p>
          </div>
          <div>
            <p className="muted">Clase</p>
            <p className="preview-value">
              {formatEnum(
                availableClasses.find(
                  (clase) => clase.id?.toString() === classId,
                )?.nombre,
              ) || "Sin definir"}
            </p>
          </div>
          <div>
            <p className="muted">Facción</p>
            <p className={`preview-value ${factionClass}`}>
              {formatEnum(selectedRace?.faccion) || "Sin juramento"}
            </p>
          </div>
        </div>
        <div className="info-stack mt-2">
          {raceDetail ? (
            <p>
              <strong>Raza:</strong> {formatEnum(raceDetail.nombre)} · Clases
              disponibles:{" "}
              {raceDetail.clasesDisponibles?.map(formatEnum).join(", ")}
            </p>
          ) : null}
          {classDetail ? (
            <p>
              <strong>Clase:</strong> {formatEnum(classDetail.nombre)} · Razas
              disponibles:{" "}
              {classDetail.razasDisponibles?.map(formatEnum).join(", ")}
            </p>
          ) : null}
          {factionDetail ? (
            <p>
              <strong>Facción:</strong> {formatEnum(factionDetail.nombre)}
            </p>
          ) : null}
        </div>
      </div>
    </div>
  );
}

export default CharacterCreation;
